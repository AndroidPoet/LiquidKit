# Contributing

Thanks for helping improve LiquidKit.

## Local Checks

Run the same checks used by CI before opening a pull request:

```bash
./gradlew :liquidkit:check
./gradlew :sampleApp:compileDebugKotlinAndroid
./gradlew :sampleApp:compileKotlinIosSimulatorArm64
```

For iOS app changes, regenerate or build from `iosApp` as needed:

```bash
cd iosApp
xcodegen generate
```

## Project Shape

- `:liquidkit` contains the public Kotlin Multiplatform library.
- `:sampleApp` contains shared Android/iOS sample UI.
- `iosApp` contains the native SwiftUI shell for the iOS sample.
- `buildSrc` contains shared build constants.

Keep public APIs under `io.github.androidpoet.liquidkit`. Platform-specific
implementation details should stay inside source-set-specific or internal
packages.

## Pull Requests

- Keep changes scoped to one concern.
- Include platform notes when behavior differs between Android and iOS.
- Add or update sample coverage when changing a component.
- Call out breaking API changes clearly in the PR description.
