# Netflared NeoForge

A client-side Cloudflare Access tunnel manager for **Minecraft 1.21.1 + NeoForge**.

This repository is the dedicated NeoForge 1.21.1 edition of Netflared.

## Features

- Netflared controls in the Multiplayer screen.
- **F9** shortcut for opening the Netflared menu.
- Multiple Cloudflare Access tunnel profiles.
- Automatic cloudflared download on first use.
- Per-profile hostname and local port.
- One-click connection to the local forwarded endpoint.
- Orphaned cloudflared process cleanup.
- Persistent configuration in the Minecraft config directory.
- Dynamic language-file synchronization.

## Requirements

| Component | Version |
|---|---|
| Minecraft | **1.21.1** |
| NeoForge | **21.1.235+** |
| Java | **21** |
| Gradle | **9.2.1** |

## Building

Run:

~~~bash
gradle clean build
~~~

The mod JAR is written to:

~~~text
build/libs/
~~~

For a development client:

~~~bash
gradle runClient
~~~

## Using Netflared

1. Install the built JAR in the Minecraft 1.21.1 NeoForge mods directory.
2. Open Multiplayer.
3. Open **Netflared** or press **F9**.
4. Create a profile with your Cloudflare Access hostname and local port.
5. Connect to the tunnel.
6. Use **Join** once the local endpoint is ready.

The tunnel uses:

~~~text
cloudflared access tcp --hostname <domain> --url 127.0.0.1:<port>
~~~

The cloudflared executable is downloaded automatically when needed.

## Configuration

Configuration is stored at:

~~~text
config/netflared/netflared.json
~~~

Downloaded binaries and tunnel PID files are kept in the same Netflared configuration area.

## CI

GitHub Actions builds this project with **JDK 21** and **Gradle 9.2.1** for the **1.21.1** branch.

## License

Released under the **MIT License**. See LICENSE.
