# LiquidKit

LiquidKit is a Kotlin Multiplatform component kit for Liquid Glass-style UI.

One shared Compose Multiplatform API; the rendering diverges per platform:

- **Android** renders the glass look in-Compose via an internal backdrop-capture
  and refraction (Liquid Glass) shader engine.
- **iOS** wraps **genuine native UIKit controls** (`UISwitch`, `UIButton` glass
  configuration, `UIMenu`, `UIPickerView`, `UITextField`, `UISearchTextField`,
  `UIStepper`, `UINavigationBar`, `UIVisualEffectView`) so components get the
  authentic system Liquid Glass on iOS 26, with a `UIBlurEffect` `systemMaterial`
  fallback on earlier versions.

See [Architecture](#architecture) for details.

## Install

LiquidKit is published to Maven Central as `io.github.androidpoet`.

```kotlin
// build.gradle.kts (Kotlin Multiplatform)
kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation("io.github.androidpoet:liquidkit:0.1.0")
            // Optional Navigation 3 tab-stack helpers:
            implementation("io.github.androidpoet:liquidkit-navigation3:0.1.0")
        }
    }
}
```

With a version catalog (`gradle/libs.versions.toml`):

```toml
[libraries]
liquidkit = { module = "io.github.androidpoet:liquidkit", version = "0.1.0" }
liquidkit-navigation3 = { module = "io.github.androidpoet:liquidkit-navigation3", version = "0.1.0" }
```

On iOS the library builds a static Kotlin/Native framework named `LiquidKit`
(and `LiquidKitNavigation3` for the optional module); consume it the same way as
any Compose Multiplatform framework embedded by your iOS app.

## Components

LiquidKit ships an iOS-parity component set:

- `LiquidButton` (variants via `LiquidButtonVariant`) and `LiquidFab`
- `LiquidSurface` and `LiquidGlassContainer`
- `LiquidDropdownMenu` (`LiquidMenuItem`) and `LiquidPicker` (`LiquidPickerOption`)
- `LiquidTextField` and `LiquidSearchField`
- `LiquidStepper` and `LiquidMediaControl`
- `LiquidToolbar` and `LiquidSheet`
- `LiquidToggle`, `LiquidSlider`, `LiquidSegmentedControl`
- `LiquidBottomNavigation` and `LiquidTabScaffold`
- `LiquidNav3TabScaffold` from the optional Navigation 3 module

## Architecture

LiquidKit is a single cross-platform Compose API backed by `expect`/`actual`
platform implementations:

- **Android shader** — components render their Liquid Glass appearance with an
  internal backdrop capture + refraction shader engine, entirely in Compose.
- **iOS native** — components wrap real UIKit controls through Compose interop,
  so they inherit the system's authentic Liquid Glass on iOS 26 (and degrade
  gracefully to a `UIBlurEffect` `systemMaterial` on earlier iOS).

You write one API; each platform draws it the right way.

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

Use `LiquidTabScaffold` when LiquidKit should hold the selected tab:

```kotlin
LiquidTabScaffold(items = items) { selectedTab ->
    when (selectedTab) {
        "home" -> HomeScreen()
        "search" -> SearchScreen()
        "settings" -> SettingsScreen()
    }
}
```

Hide or replace only the bottom bar when another shell owns it:

```kotlin
LiquidTabScaffold(
    items = items,
    bottomNavigation = { _, _ -> },
) { selectedTab ->
    TabContent(selectedTab)
}
```

## Navigation 3

Use `liquidkit-navigation3` only for Compose-owned bottom-navigation tab stacks.
LiquidKit handles the multiple back stacks; your app still owns `NavDisplay`.

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
screens inside each tab. If you reuse a Compose tab scaffold there, pass an empty
`bottomNavigation` slot so SwiftUI is the only tab bar.

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

LiquidKit's Android renderer includes source from
[Kyant0/AndroidLiquidGlass](https://github.com/Kyant0/AndroidLiquidGlass).

The optional Navigation 3 helper is adapted from the multiple-back-stack pattern
in [`terrakok/nav3-recipes`](https://github.com/terrakok/nav3-recipes).

See [THIRD_PARTY_NOTICES.md](THIRD_PARTY_NOTICES.md).
