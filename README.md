# Netflared NeoForge

Netflared is a client-side Cloudflare Access tunnel manager for Minecraft with NeoForge.

This repository maintains **one branch per Minecraft 1.21.x release** so each version can be ported and built independently.

## Supported branches

- 1.21
- 1.21.1
- 1.21.2
- 1.21.3
- 1.21.4
- 1.21.5
- 1.21.6
- 1.21.7
- 1.21.8
- 1.21.9
- 1.21.10
- 1.21.11

Each branch targets its matching Minecraft/NeoForge API. A branch is not considered binary-compatible with another version unless its own metadata and build have been verified.

## Releases

GitHub Actions builds all version branches together and publishes the resulting JARs to the combined **Netflared NeoForge 1.21.x** release.

Artifacts are named:

`netflared-neoforge-1.21.jar`
`netflared-neoforge-1.21.1.jar`
`...`
`netflared-neoforge-1.21.11.jar`

Sources JARs are excluded from the combined release.

## Features

- Cloudflare Access TCP tunnel management from Minecraft.
- Multiple saved tunnel profiles.
- F9 shortcut for the tunnel UI.
- Automatic cloudflared download for supported platforms.
- Connection/status screens and local endpoint information.
- Persistent configuration under `config/netflared/`.
- Dynamic language-file synchronization.

## Requirements

Each branch uses Java 21 and the NeoForge release configured in that branch's `gradle.properties`.

For local development, install the matching NeoForge development environment and Java 21.

## Build

From a checked-out version branch:

```bash
gradle clean build
```

The JAR is written to `build/libs/`.

For a client development run:

```bash
gradle runClient
```

## Usage

1. Install the matching NeoForge version for the branch you are using.
2. Put the Netflared JAR in the Minecraft `mods` directory.
3. Open the Netflared UI from the title or multiplayer screen, or press F9.
4. Add a tunnel profile with your Cloudflare Access hostname and local Minecraft port.
5. Connect through the generated local endpoint.

Netflared invokes cloudflared with an Access TCP command similar to:

```text
cloudflared access tcp --hostname <domain> --url 127.0.0.1:<port>
```

## Configuration

The main configuration file is:

```text
config/netflared/netflared.json
```

Cloudflared binaries and tunnel PID files are stored inside the same Netflared configuration directory.

## Versioning strategy

Minecraft 1.21.x contains multiple API and NeoForge changes. The project therefore keeps version-specific branches instead of pretending that all 1.21.x releases can use one binary.

When a source change is made, the relevant version branches can be ported independently. The release workflow then builds the complete version matrix into one release.

## License

Netflared is licensed under the MIT License.