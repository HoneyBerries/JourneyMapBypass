# JourneyMapEnhancements

A client-side Fabric mod that extends [JourneyMap](https://modrinth.com/mod/journeymap). It lifts the restrictions a server pushes to your JourneyMap client, adds a 3D movement speed info slot, and fixes tiny player names on the minimap edge.

JourneyMap is not bundled. You install it separately.

## Features

- **Lifts server-pushed JourneyMap restrictions.** Whatever the server sends, the client behaves as if everything is allowed:
  - JourneyMap and the minimap forced on; coordinates no longer hidden
  - Surface, topographic, biome and cave mapping enabled, with render range limits removed
  - Waypoints, in-game beacons, death points and multiplayer settings allowed
  - Radar enabled for players (including names), villagers, animals and mobs, with the radar distance and entity count limits raised (512 lateral, 320 vertical, up to 128 per category)
- **Movement Speed (3D) info slot.** A new JourneyMap info slot that shows speed in blocks per second including vertical movement. JourneyMap's built-in Movement Speed slot is horizontal only. Enable the new slot in JourneyMap's minimap info slot settings.
- **Minimap font scale fix.** Player names no longer shrink to a tiny size when a player is off the minimap and pinned to its edge. They now use the minimap's configured font scale.

## What is deliberately left alone

These are enforced by the server itself, so lifting them client-side would do nothing:

- Teleporting
- Server-filtered radar data (expanded or world player radar only shows what the server sends)
- Global-waypoints-only mode

## Requirements

- Minecraft 26.3
- Fabric Loader 0.19.5 or newer
- Java 25 or newer
- [Fabric API](https://modrinth.com/mod/fabric-api)
- [JourneyMap](https://modrinth.com/mod/journeymap) 26.3-6.0.0 or newer (developed against 26.3-6.0.10)

## Installation

1. Install Fabric Loader for Minecraft 26.3.
2. Put Fabric API, JourneyMap and the JourneyMapEnhancements jar in your `mods` folder.
3. Launch the game.

The mod only needs to be installed on the client. Because its mixins are required, the game will fail to start if a JourneyMap update changes the code they target, rather than silently doing nothing.

## Building from source

```
./gradlew build
```

The jars are written to `build/libs/`. To launch a development client, run `./gradlew runClient`.

## License

Licensed under the GNU General Public License v3.0. See [LICENSE](LICENSE).
