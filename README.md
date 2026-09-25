# Netflared NeoForge

Netflared NeoForge is the NeoForge edition of Netflared.

## Version branches

There is a dedicated branch for every supported release line from Minecraft 1.20 through 26.2:

- 1.20, 1.20.1, 1.20.2, 1.20.3, 1.20.4, 1.20.5, 1.20.6
- 1.21 through 1.21.11
- 26.0, 26.1, 26.1.1, 26.1.2, 26.2

The 1.20 branch is the 1.20.1-compatible alias build, and the 26.1.1 branch tracks the 26.1 code line while publishing a separately named 26.1.1 JAR.

## Combined build and releases

The .github/workflows/release.yml workflow builds all version branches in parallel.

The workflow:

1. Checks out the exact version branch for every matrix entry.
2. Selects the correct Java toolchain.
3. Resolves the current NeoForge dependency for that Minecraft line.
4. Handles the legacy 1.20.1 NeoForge artifact separately.
5. Requires exactly one production JAR from every build.
6. Uploads one uniquely named artifact per version.
7. Verifies that all 23 expected JARs exist before any release is created.
8. Publishes three releases:
   - v<version>-neo-1.20 — all 1.20.x JARs
   - v<version>-neo-1.21 — all 1.21.x JARs
   - v<version>-neo-26 — all 26.x JARs

A release job cannot start unless the complete build and verification stages succeed.

## Development

Use the branch matching the Minecraft version you are targeting. Java 17 is used for the older 1.20 releases, Java 21 for 1.20.5 through 1.21.x, and Java 25 for 26.x.

    gradle clean build

## License

MIT
