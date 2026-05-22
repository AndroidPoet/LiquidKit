package io.github.androidpoet.liquidkit.navigation3

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSerializable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.rememberDecoratedNavEntries
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import androidx.savedstate.compose.serialization.serializers.MutableStateSerializer
import androidx.savedstate.serialization.SavedStateConfiguration
import io.github.androidpoet.liquidkit.LiquidGlassStyle
import io.github.androidpoet.liquidkit.navigation.LiquidBottomNavigation
import io.github.androidpoet.liquidkit.navigation.LiquidNavigationItem
import kotlinx.serialization.PolymorphicSerializer

@Stable
public open class LiquidNav3State internal constructor(
    public val startRoute: NavKey,
    public val topLevelRoutes: Set<NavKey>,
    selectedRoute: MutableState<NavKey>,
    public val backStacks: Map<NavKey, NavBackStack<NavKey>>,
) {
    public var selectedRoute: NavKey by selectedRoute
        private set

    public val selectedTopLevelRoute: NavKey
        get() = selectedRoute

    public val currentBackStack: NavBackStack<NavKey>
        get() = backStacks[selectedRoute] ?: error("Stack for $selectedRoute not found.")

    internal val stacksInUse: List<NavKey>
        get() = if (selectedRoute == startRoute) {
            listOf(startRoute)
        } else {
            listOf(startRoute, selectedRoute)
        }

    public fun backStack(route: NavKey): NavBackStack<NavKey> =
        backStacks[route] ?: error("Stack for $route not found.")

    public fun selectTopLevel(route: NavKey) {
        require(route in topLevelRoutes) { "route must be one of the top-level routes." }
        selectedRoute = route
    }

    public fun select(route: NavKey) {
        if (route in topLevelRoutes) {
            selectedRoute = route
        } else {
            navigate(route)
        }
    }

    public fun navigate(route: NavKey) {
        currentBackStack.add(route)
    }

    public fun pop(): Boolean {
        val currentRoute = currentBackStack.lastOrNull() ?: return false

        return if (currentRoute == selectedRoute && selectedRoute != startRoute) {
            selectedRoute = startRoute
            true
        } else if (currentRoute != selectedRoute) {
            currentBackStack.removeLastOrNull()
            true
        } else {
            false
        }
    }

    internal fun goBack() {
        pop()
    }
}

@Stable
public class LiquidNav3TabState internal constructor(
    startRoute: NavKey,
    topLevelRoutes: Set<NavKey>,
    selectedRoute: MutableState<NavKey>,
    backStacks: Map<NavKey, NavBackStack<NavKey>>,
) : LiquidNav3State(
    startRoute = startRoute,
    topLevelRoutes = topLevelRoutes,
    selectedRoute = selectedRoute,
    backStacks = backStacks,
)

@Composable
public fun rememberLiquidNav3State(
    topLevelRoutes: List<NavKey>,
    startRoute: NavKey = topLevelRoutes.first(),
    savedStateConfiguration: SavedStateConfiguration,
): LiquidNav3State {
    require(topLevelRoutes.isNotEmpty()) { "rememberLiquidNav3State requires at least one route." }
    require(startRoute in topLevelRoutes) { "startRoute must be one of the top-level routes." }

    val rootRoutes = remember(topLevelRoutes) { topLevelRoutes.toSet() }
    val selectedRoute = rememberSerializable(
        startRoute,
        rootRoutes,
        configuration = savedStateConfiguration,
        serializer = MutableStateSerializer(PolymorphicSerializer(NavKey::class)),
    ) {
        androidx.compose.runtime.mutableStateOf(startRoute)
    }
    val backStacks = rootRoutes.associateWith { route ->
        rememberNavBackStack(savedStateConfiguration, route)
    }

    return remember(startRoute, rootRoutes) {
        LiquidNav3State(
            startRoute = startRoute,
            topLevelRoutes = rootRoutes,
            selectedRoute = selectedRoute,
            backStacks = backStacks,
        )
    }
}

@Composable
public fun rememberLiquidNav3TabState(
    items: List<LiquidNavigationItem<NavKey>>,
    startRoute: NavKey = items.first().key,
    savedStateConfiguration: SavedStateConfiguration,
): LiquidNav3TabState {
    require(items.isNotEmpty()) { "rememberLiquidNav3TabState requires at least one item." }
    require(items.any { it.key == startRoute }) { "startRoute must be one of the tab item keys." }

    val rootRoutes = remember(items) { items.map { it.key }.toSet() }
    val selectedRoute = rememberSerializable(
        startRoute,
        rootRoutes,
        configuration = savedStateConfiguration,
        serializer = MutableStateSerializer(PolymorphicSerializer(NavKey::class)),
    ) {
        androidx.compose.runtime.mutableStateOf(startRoute)
    }
    val backStacks = rootRoutes.associateWith { route ->
        rememberNavBackStack(savedStateConfiguration, route)
    }

    return remember(startRoute, rootRoutes) {
        LiquidNav3TabState(
            startRoute = startRoute,
            topLevelRoutes = rootRoutes,
            selectedRoute = selectedRoute,
            backStacks = backStacks,
        )
    }
}

@Composable
public fun rememberLiquidNav3Entries(
    state: LiquidNav3State,
    entryProvider: (NavKey) -> NavEntry<NavKey>,
): SnapshotStateList<NavEntry<NavKey>> {
    val decoratedEntries = state.backStacks.mapValues { (_, stack) ->
        rememberDecoratedNavEntries(
            backStack = stack,
            entryDecorators = listOf(rememberSaveableStateHolderNavEntryDecorator()),
            entryProvider = entryProvider,
        )
    }

    return state.stacksInUse
        .flatMap { route -> decoratedEntries[route] ?: emptyList() }
        .toMutableStateList()
}

@Composable
public fun LiquidNav3TabScaffold(
    items: List<LiquidNavigationItem<NavKey>>,
    entryProvider: (NavKey) -> NavEntry<NavKey>,
    savedStateConfiguration: SavedStateConfiguration,
    modifier: Modifier = Modifier,
    navigationModifier: Modifier = Modifier,
    style: LiquidGlassStyle = LiquidGlassStyle.NavigationBar,
    startRoute: NavKey = items.first().key,
) {
    val state = rememberLiquidNav3TabState(
        items = items,
        startRoute = startRoute,
        savedStateConfiguration = savedStateConfiguration,
    )

    LiquidNav3TabScaffold(
        items = items,
        state = state,
        entryProvider = entryProvider,
        modifier = modifier,
        navigationModifier = navigationModifier,
        style = style,
    )
}

@Composable
public fun LiquidNav3TabScaffold(
    items: List<LiquidNavigationItem<NavKey>>,
    state: LiquidNav3TabState,
    entryProvider: (NavKey) -> NavEntry<NavKey>,
    modifier: Modifier = Modifier,
    navigationModifier: Modifier = Modifier,
    style: LiquidGlassStyle = LiquidGlassStyle.NavigationBar,
) {
    require(items.isNotEmpty()) { "LiquidNav3TabScaffold requires at least one item." }

    Box(modifier = modifier) {
        LiquidNav3TabContent(
            state = state,
            entryProvider = entryProvider,
            modifier = Modifier.matchParentSize(),
        )
        LiquidBottomNavigation(
            items = items,
            selectedKey = state.selectedRoute,
            onSelected = state::select,
            modifier = navigationModifier.align(Alignment.BottomCenter),
            style = style,
        )
    }
}

@Composable
public fun BoxScope.LiquidNav3TabContent(
    state: LiquidNav3TabState,
    entryProvider: (NavKey) -> NavEntry<NavKey>,
    modifier: Modifier = Modifier,
) {
    NavDisplay(
        entries = rememberLiquidNav3Entries(
            state = state,
            entryProvider = entryProvider,
        ),
        onBack = state::goBack,
        modifier = modifier,
    )
}
