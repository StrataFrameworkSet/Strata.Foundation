#!/usr/bin/env node
'use strict';

const fs = require('fs');
const path = require('path');

const USAGE = 'usage: node await-published.js <path/to/package.json> [--registry <url>] [--timeout <seconds>]';

function parseArguments(argv) {
    const options = { manifest: null, registry: 'https://registry.npmjs.org/', timeout: 600 };
    for (let i = 0; i < argv.length; i++) {
        const arg = argv[i];
        if (arg === '--registry') options.registry = argv[++i];
        else if (arg === '--timeout') options.timeout = Number(argv[++i]);
        else if (!options.manifest) options.manifest = arg;
        else fail(USAGE);
    }
    if (!options.manifest || !options.registry || !(options.timeout > 0)) fail(USAGE);
    if (!options.registry.endsWith('/')) options.registry += '/';
    return options;
}

function fail(message) {
    console.error('await-published: ' + message);
    process.exit(1);
}

async function fetchDistTag(url, accept, tag, version) {
    const response = await fetch(url, { headers: { accept, 'cache-control': 'no-cache' } });
    if (response.status === 404) return { current: null, tarball: null };
    if (!response.ok) throw new Error(url + ' returned HTTP ' + response.status);
    const document = await response.json();
    const entry = (document.versions || {})[version];
    return {
        current: (document['dist-tags'] || {})[tag] || null,
        tarball: entry && entry.dist ? entry.dist.tarball || null : null
    };
}

async function tarballStatus(url) {
    try {
        const response = await fetch(url, { headers: { 'cache-control': 'no-cache' } });
        await response.arrayBuffer();
        return response.status;
    } catch (error) {
        return 'error(' + error.message + ')';
    }
}

const sleep = milliseconds => new Promise(resolve => setTimeout(resolve, milliseconds));

async function main() {
    const options = parseArguments(process.argv.slice(2));
    const manifest = JSON.parse(fs.readFileSync(path.resolve(options.manifest), 'utf8'));
    const match = /^(\d+\.\d+\.\d+)-snapshot\.\d+$/.exec(manifest.version || '');
    if (!manifest.name || !match) fail(options.manifest + ' does not hold a <x.y.z>-snapshot.<timestamp> version');

    const tag = 'snapshot-' + match[1];
    const stampOf = version => {
        const found = new RegExp('^' + match[1].replace(/\./g, '\\.') + '-snapshot\\.(\\d+)$').exec(version || '');
        return found ? BigInt(found[1]) : null;
    };
    const ownStamp = stampOf(manifest.version);
    const url = options.registry + manifest.name.replace('/', '%2f');
    const views = [
        ['install', 'application/vnd.npm.install-v1+json'],
        ['full', 'application/json']
    ];
    const deadline = Date.now() + options.timeout * 1000;
    const started = Date.now();

    const elapsed = () => Math.round((Date.now() - started) / 1000);

    console.log('await-published: waiting for ' + manifest.name + '@' + tag + ' -> ' + manifest.version);
    let tarball = null;
    while (true) {
        const seen = [];
        let supersededBy = null;
        for (const [name, accept] of views) {
            try {
                const result = await fetchDistTag(url, accept, tag, manifest.version);
                seen.push(name + '=' + result.current);
                tarball = tarball || result.tarball;
                const stamp = stampOf(result.current);
                if (stamp !== null && stamp > ownStamp) supersededBy = result.current;
            } catch (error) {
                seen.push(name + '=error(' + error.message + ')');
            }
        }
        if (seen.every(entry => entry.endsWith('=' + manifest.version)) && tarball) {
            console.log('await-published: ' + manifest.name + '@' + manifest.version + ' visible as ' + tag + ' after ' + elapsed() + 's');
            break;
        }
        if (supersededBy) {
            fail(manifest.name + '@' + tag + ' already points at newer ' + supersededBy + ' than ' + manifest.version +
                '; superseded by a concurrent publish to the same dist-tag');
        }
        if (Date.now() > deadline) {
            fail(manifest.name + '@' + tag + ' did not resolve to ' + manifest.version + ' within ' + options.timeout + 's (' + seen.join(', ') + ')');
        }
        console.log('await-published: ' + elapsed() + 's ' + seen.join(', '));
        await sleep(10000);
    }

    // Probe the origin with a unique query string so a 404 is never cached under the URL yarn fetches;
    // then request the plain URL, which stores a 200 at the CDN before yarn asks for it.
    const phases = [
        ['origin', () => tarball + (tarball.includes('?') ? '&' : '?') + 'probe=' + Date.now()],
        ['cdn', () => tarball]
    ];
    for (const [phase, target] of phases) {
        while (true) {
            const status = await tarballStatus(target());
            if (status === 200) break;
            if (Date.now() > deadline) {
                fail(tarball + ' still returns ' + status + ' (' + phase + ') after ' + options.timeout + 's');
            }
            console.log('await-published: ' + elapsed() + 's tarball(' + phase + ')=' + status);
            await sleep(10000);
        }
    }
    console.log('await-published: ' + tarball + ' downloadable after ' + elapsed() + 's');
}

main().catch(error => fail(error.stack || String(error)));
