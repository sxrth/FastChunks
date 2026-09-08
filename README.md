# FastChunks

FastChunks is a client-side Fabric optimization mod that adapts Minecraft's
render distance to the current FPS. It is intended to reduce stutter while
exploring and while many chunks are being processed.

## Features

- Adaptive render distance
- Configurable FPS target
- Configurable minimum/maximum render distance
- Small adjustment steps to avoid aggressive changes
- Client-side only
- No gameplay, combat or movement modifications
- No server-side installation required

## Configuration

After first launch, edit:

`config/fastchunks.json`

## Important

FastChunks cannot force a Minecraft server to generate or transmit chunks
faster. It optimizes the client-side workload around the chunks that are being
received and rendered.

## Compatibility

The CI workflow builds separate jars for every requested Minecraft release from
1.21 through 1.21.11 and 26.1 through 26.2.

Minecraft 26.1+ is unobfuscated and uses `net.fabricmc.fabric-loom` with Java 25.
Minecraft 1.21.11 and older use `net.fabricmc.fabric-loom-remap`.
