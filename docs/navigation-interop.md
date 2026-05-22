# Navigation Interop

LiquidKit is a component library, not an app navigation framework.

## Rule

- Compose owns tabs: use `LiquidTabScaffold` or `LiquidNav3TabScaffold`.
- Another router owns tabs: use `LiquidBottomNavigation`.
- iOS 26 native shell owns tabs: use SwiftUI `TabView` and host Compose tab
  roots inside each native tab.

## Nav3 Tabs

```kotlin
LiquidNav3TabScaffold(
    items = topLevelItems,
    savedStateConfiguration = nav3SavedStateConfiguration,
    entryProvider = entries,
)
```

The helper keeps one back stack per top-level item and renders the active stack
with `NavDisplay`.

## iOS 26 Tabs

SwiftUI owns the `TabView`. Compose renders only the tab root content.

Screen-stack navigation is still an app-layer decision.
