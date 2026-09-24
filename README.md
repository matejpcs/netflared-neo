# Netflared NeoForge

Netflared NeoForge is the NeoForge edition of Netflared, maintained as a multi-version Minecraft 1.21.x project.

## Version branches

The repository keeps a dedicated branch for every Minecraft version from 1.21 through 1.21.11:

`1.21`, `1.21.1`, `1.21.2`, `1.21.3`, `1.21.4`, `1.21.5`, `1.21.6`, `1.21.7`, `1.21.8`, `1.21.9`, `1.21.10`, and `1.21.11`.

Each branch has its own Minecraft/NeoForge dependency configuration and can contain version-specific source changes. This is intentional: the 1.21.x line contains multiple API and loader changes, so a single binary must not be assumed to work everywhere.

## Combined release

The `Build and release all NeoForge 1.21.x` GitHub Actions workflow builds every version branch and publishes the resulting JARs together in the `Netflared NeoForge 1.21.x` release.

A release is published only when all version builds succeed.

## Development

Check out the branch matching the Minecraft version you are targeting and use Java 21 with its configured NeoForge development environment.

```bash
gradle clean build
```

See the version branch README for version-specific details.

## License

MIT