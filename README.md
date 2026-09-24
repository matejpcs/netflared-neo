# Netflared

Client-side Cloudflare Access tunnel manager for Minecraft **1.21.1 + NeoForge**.

## Features
- Netflared button on the Multiplayer screen.
- F9 shortcut from the title/multiplayer screens.
- Multiple Cloudflare Access TCP tunnel profiles.
- Automatic cloudflared download on first connection.
- Per-profile hostname and local port.
- One-click join through the local forwarded port.
- Orphan-process cleanup on startup and shutdown.

## Requirements
- Minecraft 1.21.1
- NeoForge 21.1.235+
- Java 21

## Build
Run `gradle build` with JDK 21. The jar is written to `build/libs/`.

## Tunnel command
```
cloudflared access tcp --hostname <domain> --url localhost:<port>
```
