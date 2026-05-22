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
- `LiquidNav3TabScaffold` from the optional `liquidkit-navigation3` module

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
- Apps that use Navigation 3 tab stacks can use the optional
  `liquidkit-navigation3` helper module to hide the multiple-back-stack
  boilerplate.

See [Navigation Interop](docs/navigation-interop.md) for the detailed
Navigation 3 and iOS 26 ownership model.

## Project Structure

```text
.
├── liquidkit/        # Public Kotlin Multiplatform library
├── liquidkit-navigation3/  # Optional Navigation 3 helper module
├── sampleApp/              # Shared Android/iOS Compose sample
├── iosApp/                 # Native SwiftUI shell for the iOS sample
├── buildSrc/               # Shared Gradle build constants
├── docs/                   # Architecture and integration notes
├── scripts/                # Reusable Gradle publishing scripts
├── .github/                # CI, publish verification, templates, ownership
└── gradle/                 # Version catalog and Gradle wrapper files
```

## Sample App

The sample app demonstrates the public components and the production navigation
split.

Common Compose sample:

- renders `LiquidBottomNavigation`, `LiquidToggle`, `LiquidSlider`, and
  `LiquidSegmentedControl`;
- uses safe drawing padding so iOS content does not hide under the status bar;
- uses Navigation 3 for bottom-navigation multiple-stack behavior;
- uses `LiquidNav3TabScaffold` so the demo does not expose Nav3
  multiple-back-stack boilerplate;
- keeps one `rememberNavBackStack` per top-level tab inside the helper;
- renders routes through `NavDisplay` inside the helper;
- drives tab changes through LiquidKit bottom navigation.

iOS sample shell:

- uses SwiftUI `TabView` on iOS 26 and newer;
- wraps each tab in a native SwiftUI `NavigationStack`;
- hosts Compose tab roots through exported Kotlin `UIViewController` entry
  points;
- falls back to the full Compose sample on older iOS versions.

## Navigation 3 Bottom Navigation

Core LiquidKit does not depend on Navigation 3. Apps that want the easy
Navigation 3 tab-stack API can depend on `liquidkit-navigation3`.

The pattern is adapted from
[`terrakok/nav3-recipes`](https://github.com/terrakok/nav3-recipes):

1. Define top-level route objects that implement `NavKey`.
2. Create `LiquidNav3Tab` values for those routes.
3. Create a `SavedStateConfiguration` for route serialization.
4. Render `LiquidNav3TabScaffold`.
5. Push detail routes through `LiquidNav3TabState.navigate`.

This keeps LiquidKit as a UI library and keeps navigation ownership in the app.

```kotlin
val tabs = listOf(
    LiquidNav3Tab(
        root = HomeRoute,
        item = LiquidNavigationItem(HomeRoute, "Home", homeIcon),
    ),
    LiquidNav3Tab(
        root = SearchRoute,
        item = LiquidNavigationItem(SearchRoute, "Search", searchIcon),
    ),
)

val state = rememberLiquidNav3TabState(
    tabs = tabs,
    startRoute = HomeRoute,
    savedStateConfiguration = nav3SavedStateConfiguration,
)

val entries = entryProvider<NavKey> {
    entry<HomeRoute> {
        HomeScreen(onOpenDetail = { state.navigate(HomeDetailRoute) })
    }
    entry<HomeDetailRoute> {
        HomeDetailScreen(onBack = state::goBack)
    }
}

LiquidNav3TabScaffold(
    tabs = tabs,
    state = state,
    entryProvider = entries,
)
```

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
  :liquidkit-navigation3:check \
  :sampleApp:compileDebugKotlinAndroid \
  :sampleApp:compileKotlinIosSimulatorArm64 \
  :sampleApp:compileKotlinIosArm64
```

Build local publications:

```bash
./gradlew :liquidkit:publishAllPublicationsToLocalBuildRepository \
  :liquidkit-navigation3:publishAllPublicationsToLocalBuildRepository
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

Navigation 3 is intentionally scoped to the optional `liquidkit-navigation3`
module and the sample app. Core `liquidkit` has no Navigation 3 dependency.

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
