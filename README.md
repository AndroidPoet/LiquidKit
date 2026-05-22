# LiquidKit

LiquidKit is a Kotlin Multiplatform Liquid Glass component kit.

It gives Compose apps a small common API while each platform renders the best
native-feeling control it can:

- Android uses the vendored AndroidLiquidGlass renderer.
- iOS uses native UIKit controls through Compose interop.
- iOS 26 apps should use SwiftUI `TabView` for native Liquid Glass tabs.

## Modules

```kotlin
io.github.androidpoet.liquidkit
io.github.androidpoet.liquidkit.navigation3 // optional
```

Core `liquidkit` has no Navigation 3 dependency. Use
`liquidkit-navigation3` only when Compose owns your tabs.

## Components

- `LiquidToggle`
- `LiquidSlider`
- `LiquidSegmentedControl`
- `LiquidBottomNavigation`
- `LiquidTabScaffold`
- `LiquidNav3TabScaffold` from the optional Nav3 module

## Simple Tabs

```kotlin
val items = listOf(
    LiquidNavigationItem("home", "Home", LiquidIcon(Icons.Rounded.Home)),
    LiquidNavigationItem("search", "Search", LiquidIcon(Icons.Rounded.Search)),
    LiquidNavigationItem("settings", "Settings", LiquidIcon(Icons.Rounded.Settings)),
)

LiquidTabScaffold(items = items) { selectedTab ->
    when (selectedTab) {
        "home" -> HomeScreen()
        "search" -> SearchScreen()
        "settings" -> SettingsScreen()
    }
}
```

Use `LiquidBottomNavigation` directly when another router owns selection:

```kotlin
LiquidBottomNavigation(
    items = items,
    selectedKey = currentTab,
    onSelected = { currentTab = it },
)
```

## Navigation 3 Tabs

For Compose-owned Nav3 tabs, pass normal `LiquidNavigationItem<NavKey>` values.
LiquidKit keeps the selected tab and per-tab back stacks inside the helper.

```kotlin
val items = listOf(
    LiquidNavigationItem(HomeRoute, "Home", homeIcon),
    LiquidNavigationItem(SearchRoute, "Search", searchIcon),
)

val entries = entryProvider<NavKey> {
    entry<HomeRoute> { HomeScreen() }
    entry<SearchRoute> { SearchScreen() }
}

LiquidNav3TabScaffold(
    items = items,
    savedStateConfiguration = nav3SavedStateConfiguration,
    entryProvider = entries,
)
```

## iOS 26 Tabs

On iOS 26, keep tabs native:

- SwiftUI owns `TabView`.
- Each tab hosts one Compose root.
- Compose/Nav3 does not own the iOS tab bar.

The sample does this in `iosApp/LiquidKitSample/ContentView.swift`.

## Controls

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
    segments = segments,
    selectedKey = selectedSegment,
    onSelected = { selectedSegment = it },
)
```

## Build

```bash
./gradlew :liquidkit:check \
  :liquidkit-navigation3:check \
  :sampleApp:compileDebugKotlinAndroid \
  :sampleApp:compileKotlinIosSimulatorArm64 \
  :sampleApp:compileKotlinIosArm64
```

Install the Android sample:

```bash
./gradlew :sampleApp:installDebug
```

Build the iOS sample from:

```text
iosApp/LiquidKitSample.xcodeproj
```

## Credits

LiquidKit's Android renderer includes source from
[Kyant0/AndroidLiquidGlass](https://github.com/Kyant0/AndroidLiquidGlass).

The optional Navigation 3 tab helper is adapted from the multiple-back-stack
pattern in [`terrakok/nav3-recipes`](https://github.com/terrakok/nav3-recipes).

See [THIRD_PARTY_NOTICES.md](THIRD_PARTY_NOTICES.md).
