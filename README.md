# LiquidKit

LiquidKit is a Kotlin Multiplatform component kit for Liquid Glass UI.

The public API lives in common code. Android uses copied AndroidLiquidGlass
renderer/component source internally, while iOS uses native UIKit controls
through Compose Multiplatform interop.

## Components

- `LiquidIcon`
- `LiquidToggle`
- `LiquidSlider`
- `LiquidSegmentedControl`
- `LiquidBottomNavigation`
- `LiquidNavigationScaffold`

## Architecture

LiquidKit keeps component behavior in `commonMain` and isolates glass rendering
behind a single platform surface.

- Android vendors the upstream `com.kyant.backdrop` renderer plus the catalog
  bottom tabs and toggle controls used by AndroidLiquidGlass.
- iOS uses native `UITabBar`, `UISwitch`, `UISlider`, and `UISegmentedControl`
  through Compose Multiplatform UIKit interop.
- Shared components keep one common API, but controls with platform-native
  behavior use platform actual renderers.
- `LiquidNavigationScaffold` owns bottom-navigation selection state when an app
  only needs tab switching. Full iOS 26 `TabView`/`NavigationStack` ownership
  still belongs in the native app shell when the app needs Apple's system
  Liquid Glass navigation behavior.

## Sample App

The `:sampleApp` module is split intentionally:

- Android and iOS both run the common sample, which exercises
  `LiquidBottomNavigation`, `LiquidToggle`, `LiquidSlider`, and
  `LiquidSegmentedControl`.
- Android renders the controls with the vendored AndroidLiquidGlass
  implementation.
- iOS renders the controls with native UIKit interop.
- The `iosApp` target follows the iOS 26 migration pattern: SwiftUI owns the
  native `TabView` and per-tab `NavigationStack`, while Compose renders each tab
  root through exported Kotlin view controller entry points.

```bash
gradle :sampleApp:compileDebugKotlinAndroid :sampleApp:compileKotlinIosSimulatorArm64
gradle :sampleApp:installDebug
cd iosApp && xcodegen generate
```

## Credits

LiquidKit's Android renderer copies source from
[Kyant0/AndroidLiquidGlass](https://github.com/Kyant0/AndroidLiquidGlass).
Credit to Kyant for the Android Liquid Glass research and implementation.

See [THIRD_PARTY_NOTICES.md](THIRD_PARTY_NOTICES.md) for vendored source
attribution.

## Package

```kotlin
io.github.androidpoet.liquidkit
```

## Example

```kotlin
val items = listOf(
    LiquidNavigationItem("home", "Home", icon = LiquidIcon(Icons.Rounded.Home)),
    LiquidNavigationItem("search", "Search", icon = LiquidIcon(Icons.Rounded.Search)),
    LiquidNavigationItem("settings", "Settings", icon = LiquidIcon(Icons.Rounded.Settings)),
)

LiquidBottomNavigation(
    items = items,
    selectedKey = "home",
    onSelected = { key -> /* update state */ },
)
```

```kotlin
LiquidNavigationScaffold(items = items) { selectedKey ->
    when (selectedKey) {
        "home" -> HomeScreen()
        "search" -> SearchScreen()
        "settings" -> SettingsScreen()
    }
}
```

```kotlin
LiquidToggle(
    checked = enabled,
    onCheckedChange = { enabled = it },
)
```
