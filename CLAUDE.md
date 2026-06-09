# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project

Tapsell Mediation Android sample apps demonstrating Tapsell Mediation SDK integration for mobile ad mediation. Contains
3 sample apps (Java, Kotlin, Jetpack Compose) sharing a common library module.

## Build Commands

```bash
# Build everything
./gradlew build

# Build a specific sample module
./gradlew :sample-kotlin:assembleDebug
./gradlew :sample-java:assembleDebug
./gradlew :sample-jetpack-compose:assembleDebug

# Run instrumentation tests (requires emulator/device)
./gradlew connectedDebugAndroidTest

# Run unit tests only
./gradlew test

# Lint
./gradlew lint

# Clean
./gradlew clean
```

## Architecture

```
├── shared/                   # Android library: constants, ad network zone IDs, ConsoleView widget
├── sample-kotlin/            # Single Activity + Fragment + ViewModel + ViewBinding (Navigation component)
├── sample-java/              # Multiple Activity pattern
├── sample-jetpack-compose/   # Single Activity + Compose + ViewModel (Navigation Compose)
├── build-logic/              # Included build: custom TapsellAppPlugin (applies AGP, sets compileSdk/targetSdk=35, configures signing)
├── maestro/                  # Maestro UI test flows (rewarded, interstitial, native, banner, preroll)
└── gradle/libs.versions.toml # Central version catalog for all dependencies
```

- **All ad network zone IDs** are in `shared/src/main/java/ir/tapsell/shared/TapsellKeys.kt` (sealed interface per
  network).
- **App credentials** (`TAPSELL_APP_ID`, `ADMOB_APP_ID`) are set in root `build.gradle.kts` via
  `TapsellManifestPlaceholders` object and injected as manifest placeholders.
- **App signing** uses a dev keystore from `.credential/dev-keystore.properties` (applied by `TapsellAppPlugin` to all
  build types).
- **Version catalog**: `gradle/libs.versions.toml` controls all dependency versions. Tapsell SDK version is in
  `[versions]` block as `tapsell`.
- **Kotlin sample** (`sample-kotlin`) is the most complete sample. Each ad format (rewarded, interstitial, native,
  banner, preroll, app-open) has its own Fragment + ViewModel pair. `BaseViewModel` provides shared logging via
  `StateFlow`.

## CI

- **build.yml**: `./gradlew build` on PR/push to master, uploads release APKs
- **test.yml**: Instrumentation tests on API levels 22, 26, 31
- **danger.yml**: Enforces PR labels, description, conventional commits; warns on `libs.versions.toml` changes
- Multiple security scanning workflows (CodeQL, MobSF, Scorecard)

## Maestro Tests

UI test flows live in `maestro/`. Run with Maestro CLI against a connected device/emulator.
