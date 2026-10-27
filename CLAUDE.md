# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## What this is

A client-side Fabric mod (Minecraft 26.3, Java 25) that mixes into JourneyMap to lift the restrictions a server pushes to the client (disabled minimap, cave/topo mapping, radar, waypoints, hidden coordinates, etc.). JourneyMap is a runtime dependency installed separately by the end user; it is never bundled.

## Commands

- Build: `./gradlew build` (jars in `build/libs/`; CI runs the same command)
- Run a dev client: `./gradlew runClient` (the `run/` dir is the dev game directory)
- There are no tests and no linter configured.

## Architecture

- Split source sets (`loom.splitEnvironmentSourceSets()`): `src/main` (common entrypoint, essentially empty) and `src/client` (all real logic). Both are registered as one mod, `journeymapbypass`.
- The entire behaviour lives in `src/client/java/net/honeyberries/client/mixin/InternalStateHandlerMixin.java`. It uses `@ModifyVariable` on the `prop` local (`GlobalProperties`) stored in `InternalStateHandler.setStates` and overwrites its fields in place before JourneyMap fans them out to its state setters and `FeatureManager`. Restrictions the server enforces itself (teleport, server-filtered radar, global-waypoints-only) are deliberately left alone.
- Mixin config `src/client/resources/journeymapbypass.client.mixins.json` has `defaultRequire: 1`, so a mixin that fails to match (e.g. after a JourneyMap refactor) crashes at startup rather than silently doing nothing. New mixins must be registered there.
- `JourneyMapBypassClient` is an empty client entrypoint.

## JourneyMap dependency (non-obvious)

- Pulled from Modrinth Maven (`maven.modrinth:journeymap`) as `clientCompileOnly`; version is `journeymap_version` in `gradle.properties`. There is no remap step — JourneyMap's jar is already Mojang-mapped.
- JourneyMap's `Config<T>` API classes are only jar-in-jar inside the main jar, so `build.gradle.kts` extracts `META-INF/jars/journeymap-api-*.jar` into `build/journeymapApiJar/` at build time to compile against.
- When bumping Minecraft or JourneyMap, update `minecraft_version`, `fabric_api_version`, and `journeymap_version` together in `gradle.properties`, and re-verify the mixin target (`setStates`, `prop`, `GlobalProperties` field names).

## Releases

`.github/workflows/release.yml` runs on every push to `main` and creates a GitHub release `v<version>` from the `version=` line in `gradle.properties` if that tag doesn't already exist — bump `version` to cut a new release.
