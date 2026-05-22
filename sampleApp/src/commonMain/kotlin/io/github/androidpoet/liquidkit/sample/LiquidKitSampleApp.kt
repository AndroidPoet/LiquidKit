package io.github.androidpoet.liquidkit.sample

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.BasicText
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSerializable
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.github.androidpoet.liquidkit.icon.LiquidIcon
import io.github.androidpoet.liquidkit.navigation.LiquidBottomNavigation
import io.github.androidpoet.liquidkit.navigation.LiquidNavigationItem
import io.github.androidpoet.liquidkit.segmented.LiquidSegment
import io.github.androidpoet.liquidkit.segmented.LiquidSegmentedControl
import io.github.androidpoet.liquidkit.slider.LiquidSlider
import io.github.androidpoet.liquidkit.toggle.LiquidToggle
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberDecoratedNavEntries
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import androidx.savedstate.compose.serialization.serializers.MutableStateSerializer
import androidx.savedstate.serialization.SavedStateConfiguration
import kotlinx.serialization.PolymorphicSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import kotlinx.serialization.modules.subclass

public enum class LiquidKitSampleTab(
    public val title: String,
    public val iosSystemImage: String,
) {
    Home("Home", "house"),
    Search("Search", "magnifyingglass"),
    Settings("Settings", "gearshape"),
}

@Composable
fun LiquidKitSampleApp(modifier: Modifier = Modifier) {
    val navigationState = rememberLiquidKitNav3NavigationState(
        startRoute = LiquidKitHomeRoute,
        topLevelRoutes = liquidKitNav3TopLevelRoutes,
    )
    val navigator = remember(navigationState) { LiquidKitNav3Navigator(navigationState) }
    val entryProvider: (NavKey) -> NavEntry<NavKey> = entryProvider<NavKey> {
        entry<LiquidKitHomeRoute> {
            LiquidKitNav3TabRoot(
                selectedTab = LiquidKitSampleTab.Home,
                onNext = { navigator.navigate(LiquidKitHomeDetailRoute) },
            )
        }
        entry<LiquidKitSearchRoute> {
            LiquidKitNav3TabRoot(
                selectedTab = LiquidKitSampleTab.Search,
                onNext = { navigator.navigate(LiquidKitSearchDetailRoute) },
            )
        }
        entry<LiquidKitSettingsRoute> {
            LiquidKitNav3TabRoot(
                selectedTab = LiquidKitSampleTab.Settings,
                onNext = { navigator.navigate(LiquidKitSettingsDetailRoute) },
            )
        }
        entry<LiquidKitHomeDetailRoute> {
            LiquidKitNav3DetailScreen(
                selectedTab = LiquidKitSampleTab.Home,
                title = "Home Detail",
                onBack = navigator::goBack,
            )
        }
        entry<LiquidKitSearchDetailRoute> {
            LiquidKitNav3DetailScreen(
                selectedTab = LiquidKitSampleTab.Search,
                title = "Search Detail",
                onBack = navigator::goBack,
            )
        }
        entry<LiquidKitSettingsDetailRoute> {
            LiquidKitNav3DetailScreen(
                selectedTab = LiquidKitSampleTab.Settings,
                title = "Settings Detail",
                onBack = navigator::goBack,
            )
        }
    }

    androidx.compose.foundation.layout.Box(
        modifier = modifier
            .fillMaxSize()
            .background(sampleBackground()),
    ) {
        NavDisplay(
            entries = navigationState.toEntries(entryProvider),
            onBack = navigator::goBack,
            modifier = Modifier.fillMaxSize(),
        )
        LiquidBottomNavigation(
            items = rememberLiquidKitNav3NavigationItems(),
            selectedKey = navigationState.topLevelRoute,
            onSelected = navigator::navigate,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .navigationBarsPadding()
                .padding(16.dp),
        )
    }
}

@Composable
public fun LiquidKitSampleTabContent(
    selectedTab: LiquidKitSampleTab,
    modifier: Modifier = Modifier,
    extraContent: @Composable ColumnScope.() -> Unit = {},
) {
    var notificationsEnabled by remember { mutableStateOf(true) }
    var compactModeEnabled by remember { mutableStateOf(false) }
    var intensity by remember { mutableStateOf(0.58f) }
    var density by remember { mutableStateOf(ControlDensity.Regular) }
    val densitySegments = rememberControlDensitySegments()

    Column(
        modifier = modifier
            .fillMaxSize()
            .safeDrawingPadding()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp, vertical = 30.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(24.dp),
    ) {
        Header(
            selectedTab = selectedTab,
            modifier = Modifier
                .fillMaxWidth()
                .widthIn(max = 620.dp),
        )

        ComponentPanel(
            modifier = Modifier
                .fillMaxWidth()
                .widthIn(max = 620.dp),
        ) {
            LiquidKitToggleRow(
                label = "LiquidKit Toggle",
                description = "Rendered by AndroidLiquidGlass on Android and UISwitch on iOS.",
                value = notificationsEnabled,
                onValueChange = { notificationsEnabled = it },
            )
            PanelDivider()
            LiquidKitToggleRow(
                label = "LiquidKit Compact Toggle",
                description = "Same common API, platform-native renderer underneath.",
                value = compactModeEnabled,
                onValueChange = { compactModeEnabled = it },
            )
            PanelDivider()
            LiquidKitSliderRow(
                label = "LiquidKit Slider",
                description = "Android uses AndroidLiquidGlass slider; iOS uses UISlider.",
                value = intensity,
                onValueChange = { intensity = it },
            )
            PanelDivider()
            LiquidKitSegmentedRow(
                label = "LiquidKit Segmented Control",
                description = "Compact segmented choices with native UISegmentedControl on iOS.",
                segments = densitySegments,
                selected = density,
                onSelected = { density = it },
            )
        }

        extraContent()

        Spacer(modifier = Modifier.height(96.dp))
    }
}

@Composable
public fun LiquidKitSampleTabRoot(
    selectedTab: LiquidKitSampleTab,
    modifier: Modifier = Modifier,
) {
    LiquidKitSampleTabContent(
        selectedTab = selectedTab,
        modifier = modifier
            .fillMaxSize()
            .background(sampleBackground()),
    )
}

@Serializable
private data object LiquidKitHomeRoute : NavKey

@Serializable
private data object LiquidKitHomeDetailRoute : NavKey

@Serializable
private data object LiquidKitSearchRoute : NavKey

@Serializable
private data object LiquidKitSearchDetailRoute : NavKey

@Serializable
private data object LiquidKitSettingsRoute : NavKey

@Serializable
private data object LiquidKitSettingsDetailRoute : NavKey

private val liquidKitNav3SavedStateConfiguration = SavedStateConfiguration {
    serializersModule = SerializersModule {
        polymorphic(NavKey::class) {
            subclass(LiquidKitHomeRoute::class, LiquidKitHomeRoute.serializer())
            subclass(LiquidKitHomeDetailRoute::class, LiquidKitHomeDetailRoute.serializer())
            subclass(LiquidKitSearchRoute::class, LiquidKitSearchRoute.serializer())
            subclass(LiquidKitSearchDetailRoute::class, LiquidKitSearchDetailRoute.serializer())
            subclass(LiquidKitSettingsRoute::class, LiquidKitSettingsRoute.serializer())
            subclass(LiquidKitSettingsDetailRoute::class, LiquidKitSettingsDetailRoute.serializer())
        }
    }
}

private val liquidKitNav3TopLevelRoutes: Set<NavKey> = linkedSetOf(
    LiquidKitHomeRoute,
    LiquidKitSearchRoute,
    LiquidKitSettingsRoute,
)

@Composable
private fun rememberLiquidKitNav3NavigationItems(): List<LiquidNavigationItem<NavKey>> =
    remember {
        listOf(
            LiquidNavigationItem(
                key = LiquidKitHomeRoute,
                label = LiquidKitSampleTab.Home.title,
                icon = LiquidIcon(
                    imageVector = SampleIcons.Home,
                    contentDescription = LiquidKitSampleTab.Home.title,
                    iosSystemName = LiquidKitSampleTab.Home.iosSystemImage,
                    selectedIosSystemName = "house.fill",
                ),
            ),
            LiquidNavigationItem(
                key = LiquidKitSearchRoute,
                label = LiquidKitSampleTab.Search.title,
                icon = LiquidIcon(
                    imageVector = SampleIcons.Controls,
                    contentDescription = LiquidKitSampleTab.Search.title,
                    iosSystemName = LiquidKitSampleTab.Search.iosSystemImage,
                ),
            ),
            LiquidNavigationItem(
                key = LiquidKitSettingsRoute,
                label = LiquidKitSampleTab.Settings.title,
                icon = LiquidIcon(
                    imageVector = SampleIcons.Menu,
                    contentDescription = LiquidKitSampleTab.Settings.title,
                    iosSystemName = LiquidKitSampleTab.Settings.iosSystemImage,
                    selectedIosSystemName = "gearshape.fill",
                ),
            ),
        )
    }

@Composable
private fun rememberLiquidKitNav3NavigationState(
    startRoute: NavKey,
    topLevelRoutes: Set<NavKey>,
): LiquidKitNav3NavigationState {
    val topLevelRoute = rememberSerializable(
        startRoute,
        topLevelRoutes,
        configuration = liquidKitNav3SavedStateConfiguration,
        serializer = MutableStateSerializer(PolymorphicSerializer(NavKey::class)),
    ) {
        mutableStateOf(startRoute)
    }
    val backStacks = topLevelRoutes.associateWith { route ->
        rememberNavBackStack(liquidKitNav3SavedStateConfiguration, route)
    }

    return remember(startRoute, topLevelRoutes) {
        LiquidKitNav3NavigationState(
            startRoute = startRoute,
            topLevelRoute = topLevelRoute,
            backStacks = backStacks,
        )
    }
}

private class LiquidKitNav3NavigationState(
    val startRoute: NavKey,
    topLevelRoute: androidx.compose.runtime.MutableState<NavKey>,
    val backStacks: Map<NavKey, NavBackStack<NavKey>>,
) {
    var topLevelRoute: NavKey by topLevelRoute

    val stacksInUse: List<NavKey>
        get() = if (topLevelRoute == startRoute) {
            listOf(startRoute)
        } else {
            listOf(startRoute, topLevelRoute)
        }
}

private class LiquidKitNav3Navigator(
    private val state: LiquidKitNav3NavigationState,
) {
    fun navigate(route: NavKey) {
        if (route in state.backStacks.keys) {
            state.topLevelRoute = route
        } else {
            state.backStacks[state.topLevelRoute]?.add(route)
        }
    }

    fun goBack() {
        val currentStack = state.backStacks[state.topLevelRoute]
            ?: error("Stack for ${state.topLevelRoute} not found.")
        val currentRoute = currentStack.last()

        if (currentRoute == state.topLevelRoute) {
            state.topLevelRoute = state.startRoute
        } else {
            currentStack.removeLastOrNull()
        }
    }
}

@Composable
private fun LiquidKitNav3NavigationState.toEntries(
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

@Composable
private fun LiquidKitNav3TabRoot(
    selectedTab: LiquidKitSampleTab,
    onNext: () -> Unit,
) {
    LiquidKitSampleTabContent(selectedTab = selectedTab) {
        ComponentPanel(
            modifier = Modifier
                .fillMaxWidth()
                .widthIn(max = 620.dp),
        ) {
            LiquidKitNav3ActionRow(
                label = "Nav3 ${selectedTab.title} Detail",
                description = "Pushes a route onto this tab's own Navigation 3 back stack.",
                onClick = onNext,
            )
        }
    }
}

@Composable
private fun LiquidKitNav3DetailScreen(
    selectedTab: LiquidKitSampleTab,
    title: String,
    onBack: () -> Unit,
) {
    var count by rememberSaveable { mutableIntStateOf(0) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .safeDrawingPadding()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp, vertical = 30.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(24.dp),
    ) {
        Header(
            selectedTab = selectedTab,
            modifier = Modifier
                .fillMaxWidth()
                .widthIn(max = 620.dp),
        )

        ComponentPanel(
            modifier = Modifier
                .fillMaxWidth()
                .widthIn(max = 620.dp),
        ) {
            LabelBlock(
                label = title,
                description = "This screen is rendered by NavDisplay from a tab-specific Nav3 stack.",
                modifier = Modifier.padding(vertical = 14.dp),
            )
            PanelDivider()
            LiquidKitNav3ActionRow(
                label = "Counter: $count",
                description = "State is retained by Nav3's decorated entry for this route.",
                onClick = { count++ },
            )
            PanelDivider()
            LiquidKitNav3ActionRow(
                label = "Back",
                description = "Pops this route from the current tab stack.",
                onClick = onBack,
            )
        }

        Spacer(modifier = Modifier.height(96.dp))
    }
}

@Composable
private fun LiquidKitNav3ActionRow(
    label: String,
    description: String,
    onClick: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 14.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        LabelBlock(
            label = label,
            description = description,
            modifier = Modifier.weight(1f),
        )
    }
}

@Composable
private fun Header(
    selectedTab: LiquidKitSampleTab,
    modifier: Modifier = Modifier,
) {
    val subtitle = when (selectedTab) {
        LiquidKitSampleTab.Home -> "Bottom navigation, toggle, slider, and segmented controls."
        LiquidKitSampleTab.Search -> "Android uses vendored AndroidLiquidGlass controls."
        LiquidKitSampleTab.Settings -> "iOS uses native TabView, NavigationStack, and UIKit controls."
    }

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        BasicText(
            text = "LiquidKit",
            style = TextStyle(
                color = Color(0xFF0F1720),
                fontSize = 34.sp,
                fontWeight = FontWeight.Bold,
                lineHeight = 38.sp,
            ),
        )
        BasicText(
            text = subtitle,
            style = TextStyle(
                color = Color(0xFF4A5868),
                fontSize = 15.sp,
                lineHeight = 21.sp,
            ),
        )
    }
}

@Composable
private fun ComponentPanel(
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit,
) {
    Column(
        modifier = modifier
            .background(
                color = Color(0xCCFFFFFF),
                shape = RoundedCornerShape(28.dp),
            )
            .border(
                width = 1.dp,
                color = Color(0x80FFFFFF),
                shape = RoundedCornerShape(28.dp),
            )
            .padding(horizontal = 18.dp, vertical = 8.dp),
        content = content,
    )
}

@Composable
private fun PanelDivider() {
    Spacer(
        modifier = Modifier
            .fillMaxWidth()
            .height(1.dp)
            .background(Color(0x1F4A5868)),
    )
}

private enum class ControlDensity(
    val title: String,
) {
    Compact("Compact"),
    Regular("Regular"),
    Spacious("Spacious"),
}

@Composable
private fun rememberControlDensitySegments(): List<LiquidSegment<ControlDensity>> =
    remember {
        ControlDensity.entries.map { density ->
            LiquidSegment(
                key = density,
                label = density.title,
            )
        }
    }

@Composable
private fun LiquidKitToggleRow(
    label: String,
    description: String,
    value: Boolean,
    onValueChange: (Boolean) -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 14.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        LabelBlock(
            label = label,
            description = description,
            modifier = Modifier.weight(1f),
        )
        LiquidToggle(
            checked = value,
            onCheckedChange = onValueChange,
            modifier = Modifier.size(width = 62.dp, height = 36.dp),
        )
    }
}

@Composable
private fun LiquidKitSliderRow(
    label: String,
    description: String,
    value: Float,
    onValueChange: (Float) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            LabelBlock(
                label = label,
                description = description,
                modifier = Modifier.weight(1f),
            )
            BasicText(
                text = "${(value * 100).toInt()}%",
                style = TextStyle(
                    color = Color(0xFF111820),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                ),
            )
        }
        LiquidSlider(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.fillMaxWidth(),
        )
    }
}

@Composable
private fun LiquidKitSegmentedRow(
    label: String,
    description: String,
    segments: List<LiquidSegment<ControlDensity>>,
    selected: ControlDensity,
    onSelected: (ControlDensity) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        LabelBlock(
            label = label,
            description = description,
        )
        LiquidSegmentedControl(
            segments = segments,
            selectedKey = selected,
            onSelected = onSelected,
            modifier = Modifier.fillMaxWidth(),
        )
    }
}

@Composable
private fun LabelBlock(
    label: String,
    description: String,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        BasicText(
            text = label,
            style = TextStyle(
                color = Color(0xFF111820),
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
            ),
        )
        BasicText(
            text = description,
            style = TextStyle(
                color = Color(0xFF43515F),
                fontSize = 13.sp,
                lineHeight = 18.sp,
            ),
        )
    }
}

private fun sampleBackground(): Brush = Brush.linearGradient(
    colors = listOf(
        Color(0xFFEAF5F7),
        Color(0xFFF6ECE5),
        Color(0xFFE9EDF8),
    ),
)
