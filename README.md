# LiquidGlass

LiquidGlass is a Kotlin Multiplatform component kit for Liquid Glass-style UI.

Current Maven/module coordinates still use `liquidkit` until a deliberate
breaking rename is made.

```kotlin
io.github.androidpoet.liquidkit
io.github.androidpoet.liquidkit.navigation3 // optional
```

## What You Get

- `LiquidToggle`
- `LiquidSlider`
- `LiquidSegmentedControl`
- `LiquidBottomNavigation`
- `LiquidTabScaffold`
- Optional Navigation 3 helpers for bottom-navigation tab stacks

Android uses the vendored AndroidLiquidGlass renderer. iOS uses native UIKit
controls through Compose interop.

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

Use `LiquidTabScaffold` when you want LiquidGlass to hold the selected tab:

```kotlin
LiquidTabScaffold(items = items) { selectedTab ->
    when (selectedTab) {
        "home" -> HomeScreen()
        "search" -> SearchScreen()
        "settings" -> SettingsScreen()
    }
}
```

## Navigation 3 Tabs

Use `liquidkit-navigation3` when Compose owns bottom-navigation tab stacks.
LiquidGlass handles the tab stacks; you still use normal `NavDisplay`.

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

Or use the ready-made bottom-tab shell:

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

iOS 26 apps that want native Liquid Glass tabs should keep `TabView` in SwiftUI
and host Compose screens inside each tab.

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

LiquidGlass's Android renderer includes source from
[Kyant0/AndroidLiquidGlass](https://github.com/Kyant0/AndroidLiquidGlass).

The optional Navigation 3 tab helper is adapted from the multiple-back-stack
pattern in [`terrakok/nav3-recipes`](https://github.com/terrakok/nav3-recipes).

See [THIRD_PARTY_NOTICES.md](THIRD_PARTY_NOTICES.md).
