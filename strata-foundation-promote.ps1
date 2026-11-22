[CmdletBinding(DefaultParameterSetName = 'Run')]
param(
    [Parameter(Mandatory = $true, ParameterSetName = 'Run')][string]$Version,
    [string]$NpmSnapshot,
    [switch]$DryRun,
    [switch]$Force,
    [switch]$SkipMaven,
    [switch]$SkipNpm,
    [Parameter(Mandatory = $true, ParameterSetName = 'Help')][switch]$Help
)

$ErrorActionPreference = 'Stop'

$Owner      = 'StrataFrameworkSet'
$MavenRepo  = "https://maven.pkg.github.com/$Owner/repository"
$GroupId    = 'strata.foundation'
$GroupPath  = $GroupId.Replace('.', '/')
$Artifacts  = @(
    'strata-foundation-core',
    'strata-foundation-guice',
    'strata-foundation-spring',
    'strata-foundation-kafka',
    'strata-foundation-chronicle',
    'strata-foundation-testsuite'
)
$NpmPackage  = 'strata.foundation.core'
$NpmRegistry = 'https://registry.npmjs.org/'
$NpmVisibleTimeoutSeconds = 300
$Snapshot    = "$Version-SNAPSHOT"
$Checksums  = @{ sha1 = 'SHA1'; md5 = 'MD5'; sha256 = 'SHA256'; sha512 = 'SHA512' }
$Utf8NoBom  = New-Object System.Text.UTF8Encoding $false

function Write-Step([string]$Message) { Write-Host "==> $Message" -ForegroundColor Cyan }
function Write-Info([string]$Message) { Write-Host "    $Message" }
function Write-Plan([string]$Message) { Write-Host "    [dry-run] $Message" -ForegroundColor Yellow }
function Stop-Promote([string]$Message) { throw "strata-foundation-promote: $Message" }

function Confirm-Action([string]$Message) {
    if ($Force) { return $true }
    $answer = Read-Host "    $Message [y/N]"
    return ($answer -match '^(y|yes)$')
}

function Get-HttpStatus([string]$Url, [string]$AuthConfig) {
    $arguments = @('-s', '-o', 'NUL', '-w', '%{http_code}')
    if ($AuthConfig) { $arguments += @('-K', $AuthConfig) }
    $code = & curl.exe @arguments $Url
    return [int]$code
}

function Get-Url([string]$Url, [string]$AuthConfig) {
    $output = & curl.exe -sS -f -K $AuthConfig $Url
    if ($LASTEXITCODE -ne 0) { Stop-Promote "GET failed: $Url" }
    return ($output -join "`n")
}

function Save-Url([string]$Url, [string]$Destination, [string]$AuthConfig) {
    & curl.exe -sS -f -K $AuthConfig -o $Destination $Url
    if ($LASTEXITCODE -ne 0) { Stop-Promote "download failed: $Url" }
}

function Send-File([string]$Path, [string]$Url, [string]$AuthConfig) {
    & curl.exe -sS -f -K $AuthConfig -T $Path $Url | Out-Null
    if ($LASTEXITCODE -ne 0) { Stop-Promote "upload failed: $Url" }
}

function New-AuthConfigs([string]$WorkDir) {
    if (-not $env:REPOSITORY_USER -or -not $env:REPOSITORY_PASSWORD) {
        Stop-Promote 'REPOSITORY_USER and REPOSITORY_PASSWORD must be set'
    }
    $maven = Join-Path $WorkDir 'maven.curlrc'
    $api   = Join-Path $WorkDir 'api.curlrc'
    [System.IO.File]::WriteAllText($maven, "user = `"$($env:REPOSITORY_USER):$($env:REPOSITORY_PASSWORD)`"`n", $Utf8NoBom)
    [System.IO.File]::WriteAllText($api, (@(
        "header = `"Authorization: Bearer $($env:REPOSITORY_PASSWORD)`"",
        'header = "Accept: application/vnd.github+json"',
        'header = "X-GitHub-Api-Version: 2022-11-28"'
    ) -join "`n") + "`n", $Utf8NoBom)
    return @{ Maven = $maven; Api = $api }
}

function Get-PackageVersion([string]$ArtifactId, [string]$ApiAuth) {
    $name = "$GroupId.$ArtifactId"
    foreach ($scope in @("orgs/$Owner", "users/$Owner")) {
        $page = 1
        while ($true) {
            $url  = "https://api.github.com/$scope/packages/maven/$name/versions?per_page=100&page=$page"
            $json = & curl.exe -sS -f -K $ApiAuth $url 2>$null
            if ($LASTEXITCODE -ne 0) { break }
            $parsed   = ($json -join "`n") | ConvertFrom-Json
            $versions = @($parsed | ForEach-Object { $_ })
            if ($versions.Count -eq 0) { break }
            $match = @($versions | Where-Object { $_.name -eq $Version })
            if ($match.Count -gt 1) { Stop-Promote "multiple GitHub package versions named $Version for $name" }
            $match = $match | Select-Object -First 1
            if ($match) { return @{ Url = "https://api.github.com/$scope/packages/maven/$name/versions/$($match.id)"; Id = $match.id } }
            $page++
        }
    }
    return $null
}

function Write-Checksums([string]$Path) {
    $files = @()
    foreach ($ext in $Checksums.Keys) {
        $hash = (Get-FileHash -Path $Path -Algorithm $Checksums[$ext]).Hash.ToLowerInvariant()
        $out  = "$Path.$ext"
        [System.IO.File]::WriteAllText($out, $hash, $Utf8NoBom)
        $files += $out
    }
    return $files
}

function Invoke-MavenPromote([string]$ArtifactId, [hashtable]$Auth, [string]$WorkDir) {
    Write-Step "Maven $GroupId`:$ArtifactId $Snapshot -> $Version"
    $base = "$MavenRepo/$GroupPath/$ArtifactId"
    $dir  = Join-Path $WorkDir $ArtifactId
    New-Item -ItemType Directory -Path $dir | Out-Null

    [xml]$meta = Get-Url "$base/$Snapshot/maven-metadata.xml" $Auth.Maven
    $build = $meta.metadata.versioning.snapshot
    if (-not $build -or -not $build.timestamp -or -not $build.buildNumber) {
        Stop-Promote "no current snapshot build listed for $ArtifactId $Snapshot"
    }
    $current = "$Version-$($build.timestamp)-$($build.buildNumber)"
    Write-Info "snapshot build: $current"
    $entries = @($meta.metadata.versioning.snapshotVersions.snapshotVersion |
        Where-Object { $_.value -eq $current -and $_.extension -notmatch '\.(sha1|md5|sha256|sha512)$' })
    if ($entries.Count -eq 0) { Stop-Promote "no files listed for snapshot build $current of $ArtifactId" }

    $files = @()
    foreach ($entry in $entries) {
        $classifier = if ($entry.classifier) { "-$($entry.classifier)" } else { '' }
        $source = "$ArtifactId-$($entry.value)$classifier.$($entry.extension)"
        $target = "$ArtifactId-$Version$classifier.$($entry.extension)"
        $path   = Join-Path $dir $target
        Save-Url "$base/$Snapshot/$source" $path $Auth.Maven
        if ($entry.extension -in @('pom', 'module')) {
            $text = [System.IO.File]::ReadAllText($path)
            $text = $text.Replace($entry.value, $Version).Replace($Snapshot, $Version)
            [System.IO.File]::WriteAllText($path, $text, $Utf8NoBom)
            if ($text.Contains($Snapshot) -or $text.Contains($entry.value)) {
                Stop-Promote "$target still references $Snapshot after rewrite"
            }
            Write-Info "rewrote $target (no $Snapshot references remain)"
        }
        $files += $path
        $files += Write-Checksums $path
        Write-Info "staged $target  <=  $source"
    }

    $releasePom = "$base/$Version/$ArtifactId-$Version.pom"
    $existing   = Get-PackageVersion $ArtifactId $Auth.Api
    $pomStatus  = Get-HttpStatus $releasePom $Auth.Maven
    if ($existing -or $pomStatus -in @(200, 301, 302, 303, 307, 308)) {
        if (-not $existing) { Stop-Promote "$ArtifactId $Version exists but its GitHub package version id could not be found" }
        if ($DryRun) {
            Write-Plan "would DELETE existing $ArtifactId $Version (package version id $($existing.Id))"
        } elseif (Confirm-Action "$ArtifactId $Version already exists. Delete it and republish?") {
            & curl.exe -sS -f -K $Auth.Api -X DELETE $existing.Url | Out-Null
            if ($LASTEXITCODE -ne 0) { Stop-Promote "delete failed for $ArtifactId $Version (token needs delete:packages)" }
            Write-Info "deleted existing $ArtifactId $Version"
        } else {
            Write-Info "skipped $ArtifactId (existing $Version kept)"
            return $false
        }
    } else {
        Write-Info "$ArtifactId $Version does not exist yet"
    }

    foreach ($file in $files) {
        $url = "$base/$Version/$(Split-Path -Leaf $file)"
        if ($DryRun) { Write-Plan "would PUT $url" } else { Send-File $file $url $Auth.Maven }
    }
    if (-not $DryRun) { Write-Info "published $ArtifactId $Version ($($files.Count) files)" }
    return $true
}

function Invoke-Npm([string[]]$Arguments) {
    $output = & npm.cmd @Arguments
    if ($LASTEXITCODE -ne 0) { Stop-Promote "npm $($Arguments -join ' ') failed" }
    return $output
}

function Test-NpmVersion([string]$Ver, [string]$Registry) {
    $previous = $ErrorActionPreference
    $ErrorActionPreference = 'Continue'
    $found = & npm.cmd view "$NpmPackage@$Ver" version --registry $Registry --prefer-online 2>$null
    $code  = $LASTEXITCODE
    $ErrorActionPreference = $previous
    return ($code -eq 0 -and [bool]$found)
}

function Invoke-NpmPromote([string]$WorkDir) {
    $registry   = $NpmRegistry
    $tag        = "snapshot-$Version"
    $releaseTag = "release-$Version"
    Write-Step "npm $NpmPackage $tag -> $releaseTag ($registry)"

    $ping = Get-HttpStatus "$($registry)-/ping" $null
    if ($ping -eq 0) { Stop-Promote "npm registry $registry is not reachable" }

    $source = $NpmSnapshot
    if (-not $source) {
        $tags = (Invoke-Npm @('view', $NpmPackage, 'dist-tags', '--json', '--registry', $registry)) -join "`n" | ConvertFrom-Json
        $source = $tags.$tag
        if (-not $source) { Stop-Promote "dist-tag $tag not found for $NpmPackage on $registry" }
    }
    $stamp = [regex]::Match($source, '^' + [regex]::Escape($Version) + '-snapshot\.(\d+)$')
    if (-not $stamp.Success) { Stop-Promote "$source is not a $Version-snapshot.<timestamp> version" }
    $release = "$Version-release.$($stamp.Groups[1].Value)"
    Write-Info "source snapshot: $NpmPackage@$source"
    Write-Info "release version: $NpmPackage@$release"

    if (Test-NpmVersion $release $registry) {
        Write-Info "$NpmPackage@$release already exists; only the tags will be moved"
    } else {
        Write-Info "$NpmPackage@$release does not exist yet"
        $dir = Join-Path $WorkDir 'npm'
        New-Item -ItemType Directory -Path $dir | Out-Null
        Push-Location $dir
        try {
            $tarball = (Invoke-Npm @('pack', "$NpmPackage@$source", '--registry', $registry, '--silent')) | Select-Object -Last 1
        } finally {
            Pop-Location
        }
        $tar = Join-Path $env:SystemRoot 'System32\tar.exe'
        if (-not (Test-Path $tar)) { $tar = 'tar.exe' }
        & $tar -xzf (Join-Path $dir $tarball) -C $dir
        if ($LASTEXITCODE -ne 0) { Stop-Promote "could not extract $tarball" }

        $manifest = Join-Path $dir 'package\package.json'
        $text     = [System.IO.File]::ReadAllText($manifest)
        $pattern  = '("version"\s*:\s*")' + [regex]::Escape($source) + '"'
        if ($text -notmatch $pattern) { Stop-Promote "version $source not found in staged package.json" }
        $text = ([regex]$pattern).Replace($text, "`${1}$release`"", 1)
        [System.IO.File]::WriteAllText($manifest, $text, $Utf8NoBom)
        $staged = ([System.IO.File]::ReadAllText($manifest) | ConvertFrom-Json).version
        Write-Info "staged package.json version: $staged"

        if ($DryRun) {
            Write-Plan "would PUBLISH $NpmPackage@$release to $registry with dist-tag $releaseTag"
        } else {
            Invoke-Npm @('publish', (Join-Path $dir 'package'), '--registry', $registry, '--tag', $releaseTag) | Out-Null
            Write-Info "npm published $NpmPackage@$release (dist-tag $releaseTag); waiting for it to become visible"
            $deadline = (Get-Date).AddSeconds($NpmVisibleTimeoutSeconds)
            while (-not (Test-NpmVersion $release $registry)) {
                if ((Get-Date) -gt $deadline) {
                    Stop-Promote "$NpmPackage@$release is not visible on $registry after $NpmVisibleTimeoutSeconds s; check 'npm view $NpmPackage dist-tags' before re-running"
                }
                Start-Sleep -Seconds 15
            }
            Write-Info "published $NpmPackage@$release"
        }
    }

    foreach ($distTag in @($releaseTag, 'latest')) {
        if ($DryRun) {
            Write-Plan "would TAG $NpmPackage@$release as $distTag"
        } else {
            Invoke-Npm @('dist-tag', 'add', "$NpmPackage@$release", $distTag, '--registry', $registry) | Out-Null
            Write-Info "tagged $distTag -> $release"
        }
    }
    return $true
}

function Show-Help {
    $name = Split-Path -Leaf $PSCommandPath
    Write-Host @"
Usage:
  $name -Version <x.y.z> [-NpmSnapshot <ver>] [-DryRun] [-Force] [-SkipMaven] [-SkipNpm]
  $name -Help

Promotes published <version>-SNAPSHOT builds of Strata Foundation to release <version>.

  Maven (GitHub Packages, $MavenRepo):
    Downloads $($Artifacts -join ', ')
    at <version>-SNAPSHOT, rewrites the pom and module to <version>, and deploys them as <version>.
    An existing <version> is deleted (after confirmation) and republished.

  npm ($NpmRegistry):
    Takes $NpmPackage from dist-tag snapshot-<version> (<version>-snapshot.<timestamp>),
    publishes it as <version>-release.<timestamp>, and points dist-tags release-<version>
    and latest at it. If that release version already exists, only the tags are moved.

Parameters:
  -Version <x.y.z>     Release version to promote (required), e.g. 1.0.0
  -NpmSnapshot <ver>   npm snapshot to promote instead of the <version>-snapshot dist-tag target
  -DryRun              Download and stage everything; print deletes, uploads and tags without
                       changing any registry
  -Force               Do not prompt before deleting an existing Maven release
  -SkipMaven           Skip the Maven artifacts
  -SkipNpm             Skip the npm package
  -Help                Show this help

Environment:
  REPOSITORY_USER, REPOSITORY_PASSWORD   GitHub Packages credentials
                                         (token needs read, write and delete:packages)
  npm authentication for $NpmRegistry comes from ~/.npmrc

Examples:
  $name -Version 1.0.0 -DryRun
  $name -Version 1.0.0 -SkipNpm -Force
  $name -Version 1.0.0 -SkipMaven
"@
}

if ($Help) { Show-Help; return }

if ($Version -match '(?i)snapshot') { Stop-Promote "pass the release version (e.g. 1.0.0), not a snapshot" }

$work = Join-Path ([System.IO.Path]::GetTempPath()) ("strata-foundation-promote-" + [guid]::NewGuid().ToString('N'))
New-Item -ItemType Directory -Path $work | Out-Null
$skipped = @()
try {
    if ($DryRun) { Write-Step 'DRY RUN: no registry will be changed' }
    if (-not $SkipMaven) {
        $auth = New-AuthConfigs $work
        foreach ($artifactId in $Artifacts) {
            if (-not (Invoke-MavenPromote $artifactId $auth $work)) { $skipped += "maven:$artifactId" }
        }
    }
    if (-not $SkipNpm) {
        if (-not (Invoke-NpmPromote $work)) { $skipped += "npm:$NpmPackage" }
    }
    if ($skipped.Count -gt 0) { Write-Step "Done; skipped: $($skipped -join ', ')" } else { Write-Step 'Done' }
} finally {
    Remove-Item -Recurse -Force $work -ErrorAction SilentlyContinue
}
