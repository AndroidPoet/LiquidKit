# Navigation Interop

LiquidKit is a component library, not an app navigation framework. Its
navigation components are intentionally controlled by app state so they can sit
next to Navigation 3, native SwiftUI navigation, or another router.

## Compose Multiplatform Navigation 3

Navigation 3 owns a user-managed back stack. In a Compose app, keep the route
stack in the app layer and render destinations with `NavDisplay`. Use
`LiquidBottomNavigation` as the tab bar by deriving `selectedKey` from the
current top-level route and updating the route stack from `onSelected`.

```kotlin
LiquidBottomNavigation(
    items = topLevelItems,
    selectedKey = selectedTopLevelRoute,
    onSelected = { route ->
        replaceTopLevelRoute(route)
    },
)
```

`LiquidNavigationScaffold` is for simpler tab-driven screens. It can still
notify an external route owner through `onSelected`, but apps with full Nav3
back-stack ownership should prefer the lower-level `LiquidBottomNavigation`
when they need custom root/detail stack behavior.

For the common multiple-tab-stack case, use the optional
`liquidkit-navigation3` module instead of copying Nav3 boilerplate into the app:

```kotlin
val state = rememberLiquidNav3TabState(
    tabs = tabs,
    startRoute = HomeRoute,
    savedStateConfiguration = nav3SavedStateConfiguration,
)

LiquidNav3TabScaffold(
    tabs = tabs,
    state = state,
    entryProvider = entryProvider,
)
```

The sample app uses this shape, adapted from `terrakok/nav3-recipes` multiple
stacks:

- top-level routes are `NavKey` objects;
- each top-level route owns a `rememberNavBackStack`;
- `LiquidBottomNavigation` switches the active top-level route;
- `NavDisplay` renders decorated entries for the active stack;
- pushing a detail route adds it to the current tab stack, so each tab retains
  its own forward screen state.

## iOS 26 Liquid Glass

On iOS 26, SwiftUI should own `TabView` and `NavigationStack` so the system can
render Liquid Glass navigation chrome. Compose should render tab roots and
detail screen content only.

Use this ownership split:

- SwiftUI owns the selected tab and each tab's `NavigationStack` path.
- Compose/Nav3 owns route modeling and can request navigation.
- Detail pushes from Compose are forwarded to Swift through an `onNavigate`
  callback.
- After forwarding a detail route, remove it from the Compose back stack so
  SwiftUI remains the single source of truth for the native stack.
- Detail screens are rendered through a standalone Compose entry point that
  renders one route without drawing duplicate Compose navigation chrome.

LiquidKit should not depend on Navigation 3 for this. The app layer bridges
routes to SwiftUI, while LiquidKit keeps rendering controlled navigation UI.

## Practical Rule

- Android and non-native shells: Compose/Nav3 may own the full stack.
- iOS 26 native shell: SwiftUI owns tabs and pushes; Compose renders content and
  emits route events.
- LiquidKit components stay controlled and navigation-library-agnostic.
