# FastChunks

FastChunks is a Fabric client-side optimization mod that automatically adjusts
render distance based on current FPS.

It does not change combat, movement, server data, or gameplay mechanics.
It is designed to reduce client-side rendering pressure during exploration.

## Configuration

After launching once, edit:

`config/fastchunks.json`

Available settings:
- enabled
- targetFps
- minimumDistance
- maximumDistance
- step
- evaluationTicks

## Build

Use Java 21 for the 1.21.x profiles.

GitHub Actions builds the configured Minecraft profiles separately. A single
Minecraft jar should never claim compatibility with unrelated Minecraft versions.
