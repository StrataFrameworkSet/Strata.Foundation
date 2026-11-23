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

async function fetchDistTag(url, accept, tag) {
    const response = await fetch(url, { headers: { accept, 'cache-control': 'no-cache' } });
    if (response.status === 404) return null;
    if (!response.ok) throw new Error(url + ' returned HTTP ' + response.status);
    const document = await response.json();
    return (document['dist-tags'] || {})[tag] || null;
}

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

    console.log('await-published: waiting for ' + manifest.name + '@' + tag + ' -> ' + manifest.version);
    while (true) {
        const seen = [];
        let supersededBy = null;
        for (const [name, accept] of views) {
            try {
                const current = await fetchDistTag(url, accept, tag);
                seen.push(name + '=' + current);
                const stamp = stampOf(current);
                if (stamp !== null && stamp > ownStamp) supersededBy = current;
            } catch (error) {
                seen.push(name + '=error(' + error.message + ')');
            }
        }
        const elapsed = Math.round((Date.now() - started) / 1000);
        if (seen.every(entry => entry.endsWith('=' + manifest.version))) {
            console.log('await-published: ' + manifest.name + '@' + manifest.version + ' visible as ' + tag + ' after ' + elapsed + 's');
            return;
        }
        if (supersededBy) {
            fail(manifest.name + '@' + tag + ' already points at newer ' + supersededBy + ' than ' + manifest.version +
                '; superseded by a concurrent publish to the same dist-tag');
        }
        if (Date.now() > deadline) {
            fail(manifest.name + '@' + tag + ' did not resolve to ' + manifest.version + ' within ' + options.timeout + 's (' + seen.join(', ') + ')');
        }
        console.log('await-published: ' + elapsed + 's ' + seen.join(', '));
        await new Promise(resolve => setTimeout(resolve, 10000));
    }
}

main().catch(error => fail(error.stack || String(error)));
