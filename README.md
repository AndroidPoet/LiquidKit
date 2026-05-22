# LiquidGlass

LiquidGlass is a Kotlin Multiplatform component kit for Liquid Glass-style UI.

Android renders with the vendored AndroidLiquidGlass engine. iOS renders with
native UIKit controls through Compose interop.

> Maven/module coordinates still use `liquidkit` until a deliberate breaking
> rename is made.

```kotlin
io.github.androidpoet.liquidkit
io.github.androidpoet.liquidkit.navigation3 // optional
```

## Components

- `LiquidToggle`
- `LiquidSlider`
- `LiquidSegmentedControl`
- `LiquidBottomNavigation`
- `LiquidTabScaffold`
- `LiquidNav3TabScaffold` from the optional Navigation 3 module

## Controls

```kotlin
LiquidToggle(
    checked = enabled,
    onCheckedChange = { enabled = it },
)

LiquidSlider(
    value = intensity,
    onValueChange = { intensity = it },
    style = liquidGlassStyle(
        containerColor = Color.White.copy(alpha = 0.24f),
        selectedContentColor = Color(0xFF111820),
    ),
)

LiquidSegmentedControl(
    segments = segments,
    selectedKey = selectedSegment,
    onSelected = { selectedSegment = it },
)
```

## Bottom Navigation

```kotlin
val items = listOf(
    LiquidNavigationItem("home", "Home", LiquidIcon(Icons.Rounded.Home)),
    LiquidNavigationItem("search", "Search", LiquidIcon(Icons.Rounded.Search)),
    LiquidNavigationItem("settings", "Settings", LiquidIcon(Icons.Rounded.Settings)),
)

LiquidBottomNavigation(
    items = items,
    selectedKey = currentTab,
    onSelected = { currentTab = it },
)
```

Use `LiquidTabScaffold` when LiquidGlass should hold the selected tab:

```kotlin
LiquidTabScaffold(items = items) { selectedTab ->
    when (selectedTab) {
        "home" -> HomeScreen()
        "search" -> SearchScreen()
        "settings" -> SettingsScreen()
    }
}
```

## Navigation 3

Use `liquidkit-navigation3` only for Compose-owned bottom-navigation tab stacks.
LiquidGlass handles the multiple back stacks; your app still owns `NavDisplay`.

```kotlin
val state = rememberLiquidNav3State(
    topLevelRoutes = listOf(HomeRoute, SearchRoute),
    savedStateConfiguration = nav3SavedStateConfiguration,
)

val navEntries = rememberLiquidNav3Entries(
    state = state,
    entryProvider = entries,
)

Box {
    NavDisplay(
        entries = navEntries,
        onBack = { state.pop() },
        modifier = Modifier.matchParentSize(),
    )

    LiquidBottomNavigation(
        items = items,
        selectedKey = state.selectedTopLevelRoute,
        onSelected = state::selectTopLevel,
    )
}
```

For the common case, use the ready-made scaffold:

```kotlin
LiquidNav3TabScaffold(
    items = items,
    savedStateConfiguration = nav3SavedStateConfiguration,
    entryProvider = entries,
)
```

For native iOS 26 Liquid Glass tabs, keep `TabView` in SwiftUI and host Compose
screens inside each tab.

## Build

```bash
./gradlew :liquidkit:check :liquidkit-navigation3:check
```

Run the Android sample:

```bash
./gradlew :sampleApp:installDebug
```

Open the iOS sample:

```text
iosApp/LiquidKitSample.xcodeproj
```

## Credits

LiquidGlass's Android renderer includes source from
[Kyant0/AndroidLiquidGlass](https://github.com/Kyant0/AndroidLiquidGlass).

The optional Navigation 3 helper is adapted from the multiple-back-stack pattern
in [`terrakok/nav3-recipes`](https://github.com/terrakok/nav3-recipes).

See [THIRD_PARTY_NOTICES.md](THIRD_PARTY_NOTICES.md).
