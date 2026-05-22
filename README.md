# LiquidKit

LiquidKit is a Kotlin Multiplatform component kit for Liquid Glass UI.

The library exposes one common Compose API while each platform renders with the
best native or native-feeling implementation available:

- Android uses vendored AndroidLiquidGlass renderer/component source.
- iOS uses UIKit controls through Compose Multiplatform interop.
- iOS 26 navigation chrome is owned by native SwiftUI when the app needs system
  Liquid Glass `TabView` and `NavigationStack` behavior.

## Package

```kotlin
io.github.androidpoet.liquidkit
```

## Components

- `LiquidIcon`
- `LiquidToggle`
- `LiquidSlider`
- `LiquidSegmentedControl`
- `LiquidBottomNavigation`
- `LiquidNavigationScaffold`

## Architecture

LiquidKit keeps component behavior in `commonMain` and isolates glass rendering
behind platform source sets.

- Public API lives under `liquidkit/src/commonMain`.
- Android implementations live under `liquidkit/src/androidMain`.
- iOS implementations live under `liquidkit/src/iosMain`.
- Android vendors `com.kyant.backdrop` and internal AndroidLiquidGlass catalog
  components for rendering.
- iOS maps controls to native `UITabBar`, `UISwitch`, `UISlider`, and
  `UISegmentedControl`.
- `LiquidNavigationScaffold` is intentionally simple: it owns tab selection for
  apps that only need basic tab switching.
- Apps with Navigation 3, SwiftUI navigation, or another router should use
  `LiquidBottomNavigation` as controlled UI and keep the route stack in the app
  layer.

See [Navigation Interop](docs/navigation-interop.md) for the detailed
Navigation 3 and iOS 26 ownership model.

## Project Structure

```text
.
├── liquidkit/        # Public Kotlin Multiplatform library
├── sampleApp/        # Shared Android/iOS Compose sample
├── iosApp/           # Native SwiftUI shell for the iOS sample
├── buildSrc/         # Shared Gradle build constants
├── docs/             # Architecture and integration notes
├── scripts/          # Reusable Gradle publishing scripts
├── .github/          # CI, publish verification, templates, ownership
└── gradle/           # Version catalog and Gradle wrapper files
```

## Sample App

The sample app demonstrates the public components and the production navigation
split.

Common Compose sample:

- renders `LiquidBottomNavigation`, `LiquidToggle`, `LiquidSlider`, and
  `LiquidSegmentedControl`;
- uses safe drawing padding so iOS content does not hide under the status bar;
- uses Navigation 3 for bottom-navigation multiple-stack behavior;
- keeps one `rememberNavBackStack` per top-level tab;
- renders routes through `NavDisplay`;
- drives tab changes through `LiquidBottomNavigation`.

iOS sample shell:

- uses SwiftUI `TabView` on iOS 26 and newer;
- wraps each tab in a native SwiftUI `NavigationStack`;
- hosts Compose tab roots through exported Kotlin `UIViewController` entry
  points;
- falls back to the full Compose sample on older iOS versions.

## Navigation 3 Bottom Navigation

LiquidKit does not depend on Navigation 3 in the library module. The sample app
depends on Navigation 3 to show how a real app should integrate it.

The pattern is adapted from
[`terrakok/nav3-recipes`](https://github.com/terrakok/nav3-recipes):

1. Define top-level route objects that implement `NavKey`.
2. Create one `rememberNavBackStack` for each top-level route.
3. Keep the selected top-level route outside the stack.
4. Use `LiquidBottomNavigation(selectedKey, onSelected)` to switch tabs.
5. Use `NavDisplay` to render decorated entries from the active stack.
6. Push detail routes into the current tab's stack so each tab retains its own
   forward screen state.

This keeps LiquidKit as a UI library and keeps navigation ownership in the app.

## iOS 26 Liquid Glass Navigation

For iOS 26 Liquid Glass, the native shell should own navigation chrome:

- SwiftUI owns `TabView`.
- SwiftUI owns each tab's `NavigationStack`.
- Compose renders root and detail content.
- Compose/Nav3 can model route events.
- Detail pushes should be forwarded to SwiftUI and removed from the Compose
  back stack when using native SwiftUI push navigation.

This follows JetBrains' iOS Liquid Glass guidance:
https://kotlinlang.org/docs/multiplatform/ios-liquid-glass.html

The current sample includes the native SwiftUI tab/navigation shell and a
Compose Nav3 multiple-stack bottom-nav example. The next step for full
production iOS push navigation is bridging detail route events from Compose to
SwiftUI `NavigationStack` paths.

## Basic Usage

```kotlin
val items = listOf(
    LiquidNavigationItem("home", "Home", icon = LiquidIcon(Icons.Rounded.Home)),
    LiquidNavigationItem("search", "Search", icon = LiquidIcon(Icons.Rounded.Search)),
    LiquidNavigationItem("settings", "Settings", icon = LiquidIcon(Icons.Rounded.Settings)),
)

LiquidBottomNavigation(
    items = items,
    selectedKey = "home",
    onSelected = { key -> /* update route owner */ },
)
```

For simple tab-only screens:

```kotlin
LiquidNavigationScaffold(items = items) { selectedKey ->
    when (selectedKey) {
        "home" -> HomeScreen()
        "search" -> SearchScreen()
        "settings" -> SettingsScreen()
    }
}
```

For external route owners:

```kotlin
LiquidNavigationScaffold(
    items = items,
    onSelected = { key -> /* notify Navigation 3 or native shell */ },
) { selectedKey ->
    TabRoot(selectedKey)
}
```

Controls:

```kotlin
LiquidToggle(
    checked = enabled,
    onCheckedChange = { enabled = it },
)

LiquidSlider(
    value = intensity,
    onValueChange = { intensity = it },
)

LiquidSegmentedControl(
    segments = densitySegments,
    selectedKey = selectedDensity,
    onSelected = { selectedDensity = it },
)
```

## Build

Compile the library and sample targets:

```bash
./gradlew :liquidkit:check \
  :sampleApp:compileDebugKotlinAndroid \
  :sampleApp:compileKotlinIosSimulatorArm64 \
  :sampleApp:compileKotlinIosArm64
```

Build local publications:

```bash
./gradlew :liquidkit:publishAllPublicationsToLocalBuildRepository
```

Install the Android sample:

```bash
./gradlew :sampleApp:installDebug
```

Generate the iOS project if needed:

```bash
cd iosApp
xcodegen generate
```

## iOS Device Run Notes

The iOS sample builds through Xcode from `iosApp/LiquidKitSample.xcodeproj`.
When running on a physical device, Xcode signing must use a valid development
team and bundle identifier for that account.

The sample's SwiftUI shell is in:

```text
iosApp/LiquidKitSample/ContentView.swift
```

The exported Compose view controllers are in:

```text
sampleApp/src/iosMain/kotlin/io/github/androidpoet/liquidkit/sample/MainViewController.kt
```

## CI And Publishing

GitHub Actions are included for:

- KMP build verification;
- local publication verification;
- snapshot publication verification.

Publishing metadata is configured through:

```text
scripts/publish-module.gradle.kts
```

The current publish workflow validates local publication. Maven Central release
publishing still needs repository credentials and final release configuration.

## Dependency Notes

Navigation 3 is intentionally scoped to `sampleApp`.

The sample uses:

```text
org.jetbrains.androidx.navigation3:navigation3-ui:1.1.1
```

That artifact supports `iosArm64` and `iosSimulatorArm64`, but not `iosX64`.
For that reason, `sampleApp` targets the modern iOS device and Apple-silicon
simulator variants. The `liquidkit` library still keeps its broader platform
target set.

## Credits

LiquidKit's Android renderer copies source from
[Kyant0/AndroidLiquidGlass](https://github.com/Kyant0/AndroidLiquidGlass).
Credit to Kyant for the Android Liquid Glass research and implementation.

The Navigation 3 multiple-stack sample pattern is adapted from
[`terrakok/nav3-recipes`](https://github.com/terrakok/nav3-recipes).

See [THIRD_PARTY_NOTICES.md](THIRD_PARTY_NOTICES.md) for vendored source and
sample-pattern attribution.
