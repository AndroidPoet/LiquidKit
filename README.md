# LiquidKit

LiquidKit is a Kotlin Multiplatform component kit for Liquid Glass UI.

The public API lives in common code. Android renders with AndroidLiquidGlass
(`io.github.kyant0:backdrop`) while iOS uses native UIKit-backed glass surfaces
through Compose Multiplatform interop.

## Components

- `LiquidSurface`
- `LiquidButton`
- `LiquidToggle`
- `LiquidSlider`
- `LiquidBottomNavigation`
- `LiquidDropdown`

## Architecture

LiquidKit keeps component behavior in `commonMain` and isolates glass rendering
behind a single platform surface.

- Android uses `AndroidLiquidGlass` / `backdrop` for Liquid Glass rendering.
- iOS uses native `UIVisualEffectView` through Compose Multiplatform UIKit interop.
- Shared components compose on top of `LiquidSurface`, so new controls do not need
  duplicated platform implementations unless they need platform-native behavior.

## Credits

LiquidKit's Android Liquid Glass rendering is built on top of
[Kyant0/AndroidLiquidGlass](https://github.com/Kyant0/AndroidLiquidGlass).
Credit to Kyant for the Android backdrop and Liquid Glass effect work that
makes the Android renderer possible.

## Package

```kotlin
io.github.androidpoet.liquidkit
```

## Example

```kotlin
val items = listOf(
    LiquidNavigationItem("home", "Home"),
    LiquidNavigationItem("search", "Search"),
    LiquidNavigationItem("settings", "Settings"),
)

LiquidBottomNavigation(
    items = items,
    selectedKey = "home",
    onSelected = { key -> /* update state */ },
)
```

```kotlin
LiquidToggle(
    checked = enabled,
    onCheckedChange = { enabled = it },
)

LiquidSlider(
    value = volume,
    onValueChange = { volume = it },
)
```
