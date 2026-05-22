package io.github.androidpoet.liquidkit.navigation3

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
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

@Immutable
public data class LiquidNav3Tab(
    public val root: NavKey,
    public val item: LiquidNavigationItem<NavKey>,
) {
    init {
        require(root == item.key) { "LiquidNav3Tab root must match its LiquidNavigationItem key." }
    }
}

@Stable
public class LiquidNav3TabState internal constructor(
    public val startRoute: NavKey,
    selectedRoute: MutableState<NavKey>,
    internal val backStacks: Map<NavKey, NavBackStack<NavKey>>,
) {
    public var selectedRoute: NavKey by selectedRoute
        private set

    public val selectedBackStack: NavBackStack<NavKey>
        get() = backStacks[selectedRoute] ?: error("Stack for $selectedRoute not found.")

    internal val stacksInUse: List<NavKey>
        get() = if (selectedRoute == startRoute) {
            listOf(startRoute)
        } else {
            listOf(startRoute, selectedRoute)
        }

    public fun navigate(route: NavKey) {
        if (route in backStacks.keys) {
            selectedRoute = route
        } else {
            selectedBackStack.add(route)
        }
    }

    public fun goBack() {
        val currentRoute = selectedBackStack.last()

        if (currentRoute == selectedRoute) {
            selectedRoute = startRoute
        } else {
            selectedBackStack.removeLastOrNull()
        }
    }
}

@Composable
public fun rememberLiquidNav3TabState(
    tabs: List<LiquidNav3Tab>,
    startRoute: NavKey = tabs.first().root,
    savedStateConfiguration: SavedStateConfiguration,
): LiquidNav3TabState {
    require(tabs.isNotEmpty()) { "LiquidNav3TabScaffold requires at least one tab." }
    require(tabs.any { it.root == startRoute }) { "startRoute must be one of the tab root routes." }

    val rootRoutes = remember(tabs) { tabs.map { it.root }.toSet() }
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
            selectedRoute = selectedRoute,
            backStacks = backStacks,
        )
    }
}

@Composable
public fun LiquidNav3TabScaffold(
    tabs: List<LiquidNav3Tab>,
    entryProvider: (NavKey) -> NavEntry<NavKey>,
    savedStateConfiguration: SavedStateConfiguration,
    modifier: Modifier = Modifier,
    navigationModifier: Modifier = Modifier,
    style: LiquidGlassStyle = LiquidGlassStyle.NavigationBar,
    startRoute: NavKey = tabs.first().root,
) {
    val state = rememberLiquidNav3TabState(
        tabs = tabs,
        startRoute = startRoute,
        savedStateConfiguration = savedStateConfiguration,
    )

    LiquidNav3TabScaffold(
        tabs = tabs,
        state = state,
        entryProvider = entryProvider,
        modifier = modifier,
        navigationModifier = navigationModifier,
        style = style,
    )
}

@Composable
public fun LiquidNav3TabScaffold(
    tabs: List<LiquidNav3Tab>,
    state: LiquidNav3TabState,
    entryProvider: (NavKey) -> NavEntry<NavKey>,
    modifier: Modifier = Modifier,
    navigationModifier: Modifier = Modifier,
    style: LiquidGlassStyle = LiquidGlassStyle.NavigationBar,
) {
    require(tabs.isNotEmpty()) { "LiquidNav3TabScaffold requires at least one tab." }

    Box(modifier = modifier) {
        NavDisplay(
            entries = state.toEntries(entryProvider),
            onBack = state::goBack,
            modifier = Modifier.matchParentSize(),
        )
        LiquidBottomNavigation(
            items = tabs.map { it.item },
            selectedKey = state.selectedRoute,
            onSelected = state::navigate,
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
        entries = state.toEntries(entryProvider),
        onBack = state::goBack,
        modifier = modifier,
    )
}

@Composable
private fun LiquidNav3TabState.toEntries(
    entryProvider: (NavKey) -> NavEntry<NavKey>,
): SnapshotStateList<NavEntry<NavKey>> {
    val decoratedEntries = backStacks.mapValues { (_, stack) ->
        rememberDecoratedNavEntries(
            backStack = stack,
            entryDecorators = listOf(rememberSaveableStateHolderNavEntryDecorator<NavKey>()),
            entryProvider = entryProvider,
        )
    }

    return stacksInUse
        .flatMap { decoratedEntries[it] ?: emptyList() }
        .toMutableStateList()
}
