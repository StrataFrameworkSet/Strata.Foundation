#!/usr/bin/env bash
set -euo pipefail

VERSION=""
NPM_SNAPSHOT=""
DRY_RUN=false
FORCE=false
SKIP_MAVEN=false
SKIP_NPM=false

NAME="$(basename "$0")"

print_help() {
    cat <<EOF
Usage:
  $NAME --version <x.y.z> [--npm-snapshot <ver>] [--dry-run] [--force] [--skip-maven] [--skip-npm]
  $NAME --help

Promotes published <version>-SNAPSHOT builds of Strata Foundation to release <version>.

  Maven (GitHub Packages, https://maven.pkg.github.com/StrataFrameworkSet/repository):
    Downloads strata-foundation-core, strata-foundation-guice, strata-foundation-spring, strata-foundation-kafka, strata-foundation-chronicle, strata-foundation-testsuite
    at <version>-SNAPSHOT, rewrites the pom and module to <version>, and deploys them as <version>.
    An existing <version> is deleted (after confirmation) and republished.

  npm (https://registry.npmjs.org/):
    Takes strata.foundation.core from dist-tag snapshot-<version> (<version>-snapshot.<timestamp>),
    publishes it as <version>-release.<timestamp>, and points dist-tags release-<version>
    and latest at it. If that release version already exists, only the tags are moved.

Parameters:
  --version <x.y.z>     Release version to promote (required), e.g. 1.0.0
  --npm-snapshot <ver>  npm snapshot to promote instead of the <version>-snapshot dist-tag target
  --dry-run             Download and stage everything; print deletes, uploads and tags without
                        changing any registry
  --force               Do not prompt before deleting an existing Maven release
  --skip-maven          Skip the Maven artifacts
  --skip-npm            Skip the npm package
  -h, --help            Show this help

Environment:
  REPOSITORY_USER, REPOSITORY_PASSWORD   GitHub Packages credentials
                                         (token needs read, write and delete:packages)
  npm authentication for https://registry.npmjs.org/ comes from ~/.npmrc

Examples:
  $NAME --version 1.0.0 --dry-run
  $NAME --version 1.0.0 --skip-npm --force
  $NAME --version 1.0.0 --skip-maven
EOF
}

usage() {
    echo "usage: $NAME --version <x.y.z> [--npm-snapshot <ver>] [--dry-run] [--force] [--skip-maven] [--skip-npm]" >&2
    echo "       $NAME --help" >&2
    exit 2
}

while [ $# -gt 0 ]; do
    case "$1" in
        --version)      [ $# -ge 2 ] || usage; VERSION="$2"; shift 2 ;;
        --npm-snapshot) [ $# -ge 2 ] || usage; NPM_SNAPSHOT="$2"; shift 2 ;;
        --dry-run)      DRY_RUN=true; shift ;;
        --force)        FORCE=true; shift ;;
        --skip-maven)   SKIP_MAVEN=true; shift ;;
        --skip-npm)     SKIP_NPM=true; shift ;;
        -h|--help)      print_help; exit 0 ;;
        *)              echo "unknown option: $1" >&2; usage ;;
    esac
done
[ -n "$VERSION" ] || usage

OWNER="StrataFrameworkSet"
MAVEN_REPO="https://maven.pkg.github.com/$OWNER/repository"
GROUP_ID="strata.foundation"
GROUP_PATH="${GROUP_ID//.//}"
ARTIFACTS=(
    strata-foundation-core
    strata-foundation-guice
    strata-foundation-spring
    strata-foundation-kafka
    strata-foundation-chronicle
    strata-foundation-testsuite
)
NPM_PACKAGE="strata.foundation.core"
NPM_REGISTRY="https://registry.npmjs.org/"
NPM_VISIBLE_TIMEOUT_SECONDS=300
SNAPSHOT="$VERSION-SNAPSHOT"
CHECKSUMS=(sha512 sha256 md5 sha1)

step() { printf '\033[36m==> %s\033[0m\n' "$*"; }
info() { printf '    %s\n' "$*"; }
plan() { printf '\033[33m    [dry-run] %s\033[0m\n' "$*"; }
die()  { echo "strata-foundation-promote: $*" >&2; exit 1; }

confirm() {
    $FORCE && return 0
    local answer
    read -r -p "    $1 [y/N] " answer
    [[ "$answer" =~ ^(y|yes)$ ]]
}

http_status() {
    local url="$1"; shift
    curl -s -o /dev/null -w '%{http_code}' "$@" "$url" || true
}

regex_escape() { printf '%s' "$1" | sed 's/[][\.*^$/]/\\&/g'; }

xml_value() { printf '%s' "$1" | sed -n "s#.*<$2>\([^<]*\)</$2>.*#\1#p"; }

new_auth_configs() {
    [ -n "${REPOSITORY_USER:-}" ] && [ -n "${REPOSITORY_PASSWORD:-}" ] \
        || die "REPOSITORY_USER and REPOSITORY_PASSWORD must be set"
    MAVEN_AUTH="$WORK/maven.curlrc"
    API_AUTH="$WORK/api.curlrc"
    ( umask 077
      printf 'user = "%s:%s"\n' "$REPOSITORY_USER" "$REPOSITORY_PASSWORD" > "$MAVEN_AUTH"
      {
          printf 'header = "Authorization: Bearer %s"\n' "$REPOSITORY_PASSWORD"
          printf 'header = "Accept: application/vnd.github+json"\n'
          printf 'header = "X-GitHub-Api-Version: 2022-11-28"\n'
      } > "$API_AUTH" )
}

find_package_version_url() {
    local artifact_id="$1" name="$GROUP_ID.$1" scope page json result
    for scope in "orgs/$OWNER" "users/$OWNER"; do
        page=1
        while true; do
            json=$(curl -sS -f -K "$API_AUTH" \
                "https://api.github.com/$scope/packages/maven/$name/versions?per_page=100&page=$page" 2>/dev/null) || break
            result=$(printf '%s' "$json" | node -e '
                let s = "";
                process.stdin.on("data", d => s += d).on("end", () => {
                    const v = JSON.parse(s);
                    if (!Array.isArray(v) || v.length === 0) { process.stdout.write("END"); return; }
                    const m = v.find(x => x.name === process.argv[1]);
                    process.stdout.write(m ? String(m.id) : "");
                });' "$VERSION")
            [ "$result" = "END" ] && break
            if [ -n "$result" ]; then
                printf '%s %s' "$result" "https://api.github.com/$scope/packages/maven/$name/versions/$result"
                return 0
            fi
            page=$((page + 1))
        done
    done
    return 1
}

write_checksums() {
    local path="$1" algo
    for algo in "${CHECKSUMS[@]}"; do
        "${algo}sum" "$path" | cut -d' ' -f1 | tr -d '\n' > "$path.$algo"
        FILES+=("$path.$algo")
    done
}

verify_download() {
    local path="$1" ext="$2" ok=true
    case "$ext" in
        jar)    [ "$(head -c 2 "$path")" = "PK" ] || ok=false ;;
        pom)    grep -q '<project' "$path" || ok=false ;;
        module) [ "$(tr -d ' \t\r\n' < "$path" | head -c 1)" = "{" ] || ok=false ;;
    esac
    $ok || die "$(basename "$path") is not a valid .$ext file (starts with: $(head -c 60 "$path" | tr -d '\r\n'))"
}

maven_promote() {
    local artifact_id="$1"
    local base="$MAVEN_REPO/$GROUP_PATH/$artifact_id"
    local dir="$WORK/$artifact_id"
    local meta entries entry ext cls val classifier source target path text found
    step "Maven $GROUP_ID:$artifact_id $SNAPSHOT -> $VERSION"
    mkdir -p "$dir"

    meta=$(curl -sS -f -L -K "$MAVEN_AUTH" "$base/$SNAPSHOT/maven-metadata.xml") \
        || die "GET failed: $base/$SNAPSHOT/maven-metadata.xml"
    meta=$(printf '%s' "$meta" | tr -d '\r\n')
    local build ts bn current
    build=$(printf '%s' "$meta" | sed -n 's#.*<snapshot>\(.*\)</snapshot>.*#\1#p')
    ts=$(xml_value "$build" timestamp)
    bn=$(xml_value "$build" buildNumber)
    [ -n "$ts" ] && [ -n "$bn" ] || die "no current snapshot build listed for $artifact_id $SNAPSHOT"
    current="$VERSION-$ts-$bn"
    info "snapshot build: $current"
    entries=$(printf '%s' "$meta" | sed 's#</snapshotVersion>#&\n#g' | grep '<snapshotVersion>' || true)
    [ -n "$entries" ] || die "no snapshot files listed for $artifact_id $SNAPSHOT"

    FILES=()
    found=0
    while IFS= read -r entry; do
        ext=$(xml_value "$entry" extension)
        cls=$(xml_value "$entry" classifier)
        val=$(xml_value "$entry" value)
        [ "$val" = "$current" ] || continue
        case "$ext" in *.sha1|*.md5|*.sha256|*.sha512) continue ;; esac
        classifier=""
        [ -n "$cls" ] && classifier="-$cls"
        source="$artifact_id-$val$classifier.$ext"
        target="$artifact_id-$VERSION$classifier.$ext"
        path="$dir/$target"
        curl -sS -f -L -K "$MAVEN_AUTH" -o "$path" "$base/$SNAPSHOT/$source" || die "download failed: $base/$SNAPSHOT/$source"
        verify_download "$path" "$ext"
        if [ "$ext" = "pom" ] || [ "$ext" = "module" ]; then
            sed -i "s/$(regex_escape "$val")/$VERSION/g; s/$(regex_escape "$SNAPSHOT")/$VERSION/g" "$path"
            if grep -qF -e "$SNAPSHOT" -e "$val" "$path"; then
                die "$target still references $SNAPSHOT after rewrite"
            fi
            info "rewrote $target (no $SNAPSHOT references remain)"
        fi
        FILES+=("$path")
        write_checksums "$path"
        info "staged $target  <=  $source"
        found=$((found + 1))
    done <<< "$entries"
    [ "$found" -gt 0 ] || die "no files listed for snapshot build $current of $artifact_id"

    local release_pom="$base/$VERSION/$artifact_id-$VERSION.pom" existing pom_status id url
    existing=$(find_package_version_url "$artifact_id") || existing=""
    pom_status=$(http_status "$release_pom" -K "$MAVEN_AUTH")
    if [ -n "$existing" ] || [[ "$pom_status" =~ ^(200|301|302|303|307|308)$ ]]; then
        [ -n "$existing" ] \
            || die "$artifact_id $VERSION exists but its GitHub package version id could not be found"
        id="${existing%% *}"
        url="${existing#* }"
        if $DRY_RUN; then
            plan "would DELETE existing $artifact_id $VERSION (package version id $id)"
        elif confirm "$artifact_id $VERSION already exists. Delete it and republish?"; then
            curl -sS -f -K "$API_AUTH" -X DELETE "$url" > /dev/null \
                || die "delete failed for $artifact_id $VERSION (token needs delete:packages)"
            info "deleted existing $artifact_id $VERSION"
        else
            info "skipped $artifact_id (existing $VERSION kept)"
            return 1
        fi
    else
        info "$artifact_id $VERSION does not exist yet"
    fi

    local file
    for file in "${FILES[@]}"; do
        url="$base/$VERSION/$(basename "$file")"
        if $DRY_RUN; then
            plan "would PUT $url"
        else
            curl -sS -f -K "$MAVEN_AUTH" -T "$file" "$url" > /dev/null || die "upload failed: $url"
        fi
    done
    $DRY_RUN || info "published $artifact_id $VERSION (${#FILES[@]} files)"
    return 0
}

npm_version_exists() {
    local found
    found=$(npm view "$NPM_PACKAGE@$1" version --registry "$2" --prefer-online 2>/dev/null) || return 1
    [ -n "$found" ]
}

npm_promote() {
    local registry="$NPM_REGISTRY"
    local tag="snapshot-$VERSION"
    local release_tag="release-$VERSION"
    local source stamp release dir tarball manifest staged existing dist_tag
    step "npm $NPM_PACKAGE $tag -> $release_tag ($registry)"

    [ "$(http_status "${registry}-/ping")" != "000" ] \
        || die "npm registry $registry is not reachable"

    source="$NPM_SNAPSHOT"
    if [ -z "$source" ]; then
        source=$(npm view "$NPM_PACKAGE" dist-tags --json --registry "$registry" | node -e '
            let s = "";
            process.stdin.on("data", d => s += d).on("end", () => {
                const t = JSON.parse(s);
                process.stdout.write(t[process.argv[1]] || "");
            });' "$tag") || die "npm view dist-tags failed"
        [ -n "$source" ] || die "dist-tag $tag not found for $NPM_PACKAGE on $registry"
    fi
    stamp=$(printf '%s' "$source" | sed -n "s/^$(regex_escape "$VERSION")-snapshot\.\([0-9][0-9]*\)$/\1/p")
    [ -n "$stamp" ] || die "$source is not a $VERSION-snapshot.<timestamp> version"
    release="$VERSION-release.$stamp"
    info "source snapshot: $NPM_PACKAGE@$source"
    info "release version: $NPM_PACKAGE@$release"

    if npm_version_exists "$release" "$registry"; then
        info "$NPM_PACKAGE@$release already exists; only the tags will be moved"
    else
        info "$NPM_PACKAGE@$release does not exist yet"
        dir="$WORK/npm"
        mkdir -p "$dir"
        tarball=$(cd "$dir" && npm pack "$NPM_PACKAGE@$source" --registry "$registry" --silent | tail -n 1) \
            || die "npm pack $NPM_PACKAGE@$source failed"
        tar -xzf "$dir/$tarball" -C "$dir" || die "could not extract $tarball"

        manifest="$dir/package/package.json"
        staged=$(node -e '
            const fs = require("fs");
            const [p, from, to] = process.argv.slice(1);
            const j = JSON.parse(fs.readFileSync(p, "utf8"));
            if (j.version !== from) { console.error("version " + from + " not found in staged package.json"); process.exit(1); }
            j.version = to;
            fs.writeFileSync(p, JSON.stringify(j, null, 2) + "\n");
            process.stdout.write(j.version);' "$manifest" "$source" "$release") || die "could not rewrite staged package.json"
        info "staged package.json version: $staged"

        if $DRY_RUN; then
            plan "would PUBLISH $NPM_PACKAGE@$release to $registry with dist-tag $release_tag"
        else
            npm publish "$dir/package" --registry "$registry" --tag "$release_tag" > /dev/null \
                || die "npm publish $NPM_PACKAGE@$release failed"
            info "npm published $NPM_PACKAGE@$release (dist-tag $release_tag); waiting for it to become visible"
            local waited=0
            until npm_version_exists "$release" "$registry"; do
                [ "$waited" -lt "$NPM_VISIBLE_TIMEOUT_SECONDS" ] \
                    || die "$NPM_PACKAGE@$release is not visible on $registry after ${NPM_VISIBLE_TIMEOUT_SECONDS}s; check 'npm view $NPM_PACKAGE dist-tags' before re-running"
                sleep 15
                waited=$((waited + 15))
            done
            info "published $NPM_PACKAGE@$release"
        fi
    fi

    for dist_tag in "$release_tag" latest; do
        if $DRY_RUN; then
            plan "would TAG $NPM_PACKAGE@$release as $dist_tag"
        else
            npm dist-tag add "$NPM_PACKAGE@$release" "$dist_tag" --registry "$registry" > /dev/null \
                || die "npm dist-tag add $dist_tag failed"
            info "tagged $dist_tag -> $release"
        fi
    done
    return 0
}

shopt -s nocasematch
[[ "$VERSION" == *snapshot* ]] && die "pass the release version (e.g. 1.0.0), not a snapshot"
shopt -u nocasematch

WORK=$(mktemp -d "${TMPDIR:-/tmp}/strata-foundation-promote-XXXXXX")
trap 'rm -rf "$WORK"' EXIT
SKIPPED=()

$DRY_RUN && step "DRY RUN: no registry will be changed"
if ! $SKIP_MAVEN; then
    new_auth_configs
    for artifact_id in "${ARTIFACTS[@]}"; do
        maven_promote "$artifact_id" || SKIPPED+=("maven:$artifact_id")
    done
fi
if ! $SKIP_NPM; then
    npm_promote || SKIPPED+=("npm:$NPM_PACKAGE")
fi
if [ ${#SKIPPED[@]} -gt 0 ]; then
    step "Done; skipped: ${SKIPPED[*]}"
else
    step "Done"
fi
