package io.github.androidpoet.liquidkit.sample

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.savedstate.serialization.SavedStateConfiguration
import io.github.androidpoet.liquidkit.icon.LiquidIcon
import io.github.androidpoet.liquidkit.navigation.LiquidNavigationItem
import io.github.androidpoet.liquidkit.navigation3.LiquidNav3TabScaffold
import io.github.androidpoet.liquidkit.sample.showcase.ShowcaseEntry
import io.github.androidpoet.liquidkit.sample.showcase.ShowcaseEntryContent
import io.github.androidpoet.liquidkit.segmented.LiquidSegment
import io.github.androidpoet.liquidkit.segmented.LiquidSegmentedControl
import io.github.androidpoet.liquidkit.slider.LiquidSlider
import io.github.androidpoet.liquidkit.toggle.LiquidToggle
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
    LiquidKitComposeOwnedTabShell(modifier)
}

@Composable
public fun LiquidKitComposeOwnedTabShell(modifier: Modifier = Modifier) {
    val items = rememberLiquidKitTabItems()
    var openShowcase by remember { mutableStateOf<ShowcaseEntry?>(null) }
    val onOpenShowcase: (ShowcaseEntry) -> Unit = { openShowcase = it }

    val entryProvider: (NavKey) -> NavEntry<NavKey> =
        entryProvider<NavKey> {
            entry<LiquidKitHomeRoute> {
                LiquidKitSampleTabRoot(
                    selectedTab = LiquidKitSampleTab.Home,
                    onOpenShowcase = onOpenShowcase,
                )
            }
            entry<LiquidKitSearchRoute> {
                LiquidKitSampleTabRoot(
                    selectedTab = LiquidKitSampleTab.Search,
                    onOpenShowcase = onOpenShowcase,
                )
            }
            entry<LiquidKitSettingsRoute> {
                LiquidKitSampleTabRoot(
                    selectedTab = LiquidKitSampleTab.Settings,
                    onOpenShowcase = onOpenShowcase,
                )
            }
        }

    Box(modifier = modifier.fillMaxSize()) {
        LiquidNav3TabScaffold(
            items = items,
            entryProvider = entryProvider,
            savedStateConfiguration = liquidKitNav3SavedStateConfiguration,
            modifier =
                Modifier
                    .fillMaxSize()
                    .background(sampleBackground()),
            navigationModifier =
                Modifier
                    .navigationBarsPadding()
                    .padding(16.dp),
        )

        val showcase = openShowcase
        if (showcase != null) {
            Box(modifier = Modifier.fillMaxSize().background(sampleBackground())) {
                ShowcaseEntryContent(showcase, modifier = Modifier.fillMaxSize())
                ShowcaseBackChip(
                    onClick = { openShowcase = null },
                    modifier =
                        Modifier
                            .align(Alignment.TopStart)
                            .safeDrawingPadding()
                            .padding(16.dp),
                )
            }
        }
    }
}

@Composable
private fun ShowcaseBackChip(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier =
            modifier
                .background(Color(0xCC0A0A0A), RoundedCornerShape(20.dp))
                .border(1.dp, Color(0x33FFFFFF), RoundedCornerShape(20.dp))
                .clickable(onClick = onClick)
                .padding(horizontal = 18.dp, vertical = 10.dp),
    ) {
        BasicText(
            text = "‹ Back",
            style =
                TextStyle(
                    color = Color.White,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.SemiBold,
                ),
        )
    }
}

@Composable
public fun LiquidKitSampleTabContent(
    selectedTab: LiquidKitSampleTab,
    modifier: Modifier = Modifier,
    onOpenShowcase: (ShowcaseEntry) -> Unit = {},
    extraContent: @Composable ColumnScope.() -> Unit = {},
) {
    Column(
        modifier =
            modifier
                .fillMaxSize()
                .safeDrawingPadding()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 30.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(24.dp),
    ) {
        Header(
            selectedTab = selectedTab,
            modifier =
                Modifier
                    .fillMaxWidth()
                    .widthIn(max = 620.dp),
        )

        when (selectedTab) {
            LiquidKitSampleTab.Home -> HomeSamplePanel(onOpenShowcase = onOpenShowcase)
            LiquidKitSampleTab.Search -> ControlsSamplePanel()
            LiquidKitSampleTab.Settings -> SettingsSamplePanel()
        }

        extraContent()

        Spacer(modifier = Modifier.height(96.dp))
    }
}

@Composable
public fun LiquidKitSampleTabRoot(
    selectedTab: LiquidKitSampleTab,
    modifier: Modifier = Modifier,
    onOpenShowcase: (ShowcaseEntry) -> Unit = {},
) {
    LiquidKitSampleTabContent(
        selectedTab = selectedTab,
        onOpenShowcase = onOpenShowcase,
        modifier =
            modifier
                .fillMaxSize()
                .background(sampleBackground()),
    )
}

/**
 * A self-contained tab screen that owns its own component-showcase overlay.
 *
 * Used by the iOS native [TabView] (one Compose view controller per tab) so iOS
 * reaches the exact same full-screen component showcases as the Compose-owned
 * Android shell — keeping both platforms the same app while iOS retains its
 * native SwiftUI tab chrome.
 */
@Composable
public fun LiquidKitSampleTabScreen(
    selectedTab: LiquidKitSampleTab,
    modifier: Modifier = Modifier,
) {
    var openShowcase by remember { mutableStateOf<ShowcaseEntry?>(null) }

    Box(modifier = modifier.fillMaxSize()) {
        LiquidKitSampleTabRoot(
            selectedTab = selectedTab,
            onOpenShowcase = { openShowcase = it },
            modifier = Modifier.fillMaxSize(),
        )

        val showcase = openShowcase
        if (showcase != null) {
            Box(modifier = Modifier.fillMaxSize().background(sampleBackground())) {
                ShowcaseEntryContent(showcase, modifier = Modifier.fillMaxSize())
                ShowcaseBackChip(
                    onClick = { openShowcase = null },
                    modifier =
                        Modifier
                            .align(Alignment.TopStart)
                            .safeDrawingPadding()
                            .padding(16.dp),
                )
            }
        }
    }
}

@Composable
private fun HomeSamplePanel(onOpenShowcase: (ShowcaseEntry) -> Unit = {}) {
    var enabled by remember { mutableStateOf(true) }
    var intensity by remember { mutableStateOf(0.58f) }

    InvertedPanel(
        modifier =
            Modifier
                .fillMaxWidth()
                .widthIn(max = 620.dp),
    ) {
        BasicText(
            text = "One API. Native ownership.",
            style =
                TextStyle(
                    color = Color.White,
                    fontSize = 23.sp,
                    fontWeight = FontWeight.Bold,
                    lineHeight = 28.sp,
                ),
        )
        BasicText(
            text = "Android renders Kyant-style liquid controls. iOS keeps native tab ownership.",
            style =
                TextStyle(
                    color = Color(0xFFD7D7D7),
                    fontSize = 14.sp,
                    lineHeight = 20.sp,
                ),
        )
        Spacer(modifier = Modifier.height(10.dp))
        InvertedMetricRow(label = "Tab layer", value = "Nav3")
        InvertedMetricRow(label = "Android renderer", value = "LiquidGlass")
    }

    ComponentPanel(
        modifier =
            Modifier
                .fillMaxWidth()
                .widthIn(max = 620.dp),
    ) {
        PanelSectionTitle(
            title = "Component gallery",
            description =
                "Open any group full-screen. Each is a self-contained showcase " +
                    "(Android shader glass, iOS native controls).",
        )
        ShowcaseEntry.entries.forEach { entry ->
            PanelDivider()
            ShowcaseGalleryRow(entry = entry, onClick = { onOpenShowcase(entry) })
        }
    }

    ComponentPanel(
        modifier =
            Modifier
                .fillMaxWidth()
                .widthIn(max = 620.dp),
    ) {
        PanelSectionTitle(
            title = "Home controls",
            description = "A simple monochrome surface for checking the shared API.",
        )
        PanelDivider()
        LiquidKitToggleRow(
            label = "Liquid surface",
            description = "Toggle the sample state without changing the tab renderer.",
            value = enabled,
            onValueChange = { enabled = it },
        )
        PanelDivider()
        LiquidKitSliderRow(
            label = "Glass intensity",
            description = "Tune the shared control state used by this preview.",
            value = intensity,
            onValueChange = { intensity = it },
        )
        PanelDivider()
        StatusRow(label = "Renderer", value = "AndroidLiquidGlass")
        PanelDivider()
        StatusRow(label = "Navigation", value = "Compose Nav3 tabs")
    }
}

@Composable
private fun ControlsSamplePanel() {
    var density by remember { mutableStateOf(ControlDensity.Regular) }
    var compactModeEnabled by remember { mutableStateOf(false) }
    var intensity by remember { mutableStateOf(0.42f) }
    val densitySegments = rememberControlDensitySegments()

    ComponentPanel(
        modifier =
            Modifier
                .fillMaxWidth()
                .widthIn(max = 620.dp),
    ) {
        PanelSectionTitle(
            title = "Controls lab",
            description = "A clean black and white workbench for component states.",
        )
        PanelDivider()
        LiquidKitSegmentedRow(
            label = "Density",
            description = "Switch layout density for app-level controls.",
            segments = densitySegments,
            selected = density,
            onSelected = { density = it },
        )
        PanelDivider()
        LiquidKitToggleRow(
            label = "Compact controls",
            description = "Keep touch targets stable while reducing visual weight.",
            value = compactModeEnabled,
            onValueChange = { compactModeEnabled = it },
        )
        PanelDivider()
        LiquidKitSliderRow(
            label = "Response",
            description = "Exercise continuous input on the Android liquid slider.",
            value = intensity,
            onValueChange = { intensity = it },
        )
    }
}

@Composable
private fun SettingsSamplePanel() {
    var nativeTabs by remember { mutableStateOf(true) }
    var reducedMotion by remember { mutableStateOf(false) }
    var density by remember { mutableStateOf(ControlDensity.Regular) }
    val densitySegments = rememberControlDensitySegments()

    ComponentPanel(
        modifier =
            Modifier
                .fillMaxWidth()
                .widthIn(max = 620.dp),
    ) {
        PanelSectionTitle(
            title = "Library settings",
            description = "A focused settings surface for platform behavior.",
        )
        PanelDivider()
        LiquidKitToggleRow(
            label = "Native tabs on iOS",
            description = "Keep iOS tab ownership in SwiftUI and Android tabs in Compose.",
            value = nativeTabs,
            onValueChange = { nativeTabs = it },
        )
        PanelDivider()
        LiquidKitToggleRow(
            label = "Reduced motion",
            description = "Preview a quieter configuration for motion-sensitive users.",
            value = reducedMotion,
            onValueChange = { reducedMotion = it },
        )
        PanelDivider()
        LiquidKitSegmentedRow(
            label = "Default density",
            description = "Choose the default control spacing for host apps.",
            segments = densitySegments,
            selected = density,
            onSelected = { density = it },
        )
        PanelDivider()
        StatusRow(label = "Package", value = "LiquidKit")
    }
}

@Serializable
private data object LiquidKitHomeRoute : NavKey

@Serializable
private data object LiquidKitSearchRoute : NavKey

@Serializable
private data object LiquidKitSettingsRoute : NavKey

private val liquidKitNav3SavedStateConfiguration =
    SavedStateConfiguration {
        serializersModule =
            SerializersModule {
                polymorphic(NavKey::class) {
                    subclass(LiquidKitHomeRoute::class, LiquidKitHomeRoute.serializer())
                    subclass(LiquidKitSearchRoute::class, LiquidKitSearchRoute.serializer())
                    subclass(LiquidKitSettingsRoute::class, LiquidKitSettingsRoute.serializer())
                }
            }
    }

@Composable
private fun rememberLiquidKitTabItems(): List<LiquidNavigationItem<NavKey>> =
    remember {
        listOf(
            LiquidNavigationItem(
                key = LiquidKitHomeRoute,
                label = LiquidKitSampleTab.Home.title,
                icon =
                    LiquidIcon(
                        imageVector = SampleIcons.Home,
                        contentDescription = LiquidKitSampleTab.Home.title,
                        iosSystemName = LiquidKitSampleTab.Home.iosSystemImage,
                        selectedIosSystemName = "house.fill",
                    ),
            ),
            LiquidNavigationItem(
                key = LiquidKitSearchRoute,
                label = LiquidKitSampleTab.Search.title,
                icon =
                    LiquidIcon(
                        imageVector = SampleIcons.Controls,
                        contentDescription = LiquidKitSampleTab.Search.title,
                        iosSystemName = LiquidKitSampleTab.Search.iosSystemImage,
                    ),
            ),
            LiquidNavigationItem(
                key = LiquidKitSettingsRoute,
                label = LiquidKitSampleTab.Settings.title,
                icon =
                    LiquidIcon(
                        imageVector = SampleIcons.Menu,
                        contentDescription = LiquidKitSampleTab.Settings.title,
                        iosSystemName = LiquidKitSampleTab.Settings.iosSystemImage,
                        selectedIosSystemName = "gearshape.fill",
                    ),
            ),
        )
    }

@Composable
private fun Header(
    selectedTab: LiquidKitSampleTab,
    modifier: Modifier = Modifier,
) {
    val title =
        when (selectedTab) {
            LiquidKitSampleTab.Home -> "LiquidKit Home"
            LiquidKitSampleTab.Search -> "Controls Lab"
            LiquidKitSampleTab.Settings -> "Settings"
        }
    val subtitle =
        when (selectedTab) {
            LiquidKitSampleTab.Home -> "Bottom navigation with a single moving liquid selection."
            LiquidKitSampleTab.Search -> "Android controls rendered by vendored AndroidLiquidGlass."
            LiquidKitSampleTab.Settings -> "Platform ownership stays simple: SwiftUI tabs on iOS, Compose tabs on Android."
        }

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        BasicText(
            text = title,
            style =
                TextStyle(
                    color = Color(0xFF070707),
                    fontSize = 34.sp,
                    fontWeight = FontWeight.Bold,
                    lineHeight = 38.sp,
                ),
        )
        BasicText(
            text = subtitle,
            style =
                TextStyle(
                    color = Color(0xFF4A4A4A),
                    fontSize = 15.sp,
                    lineHeight = 21.sp,
                ),
        )
    }
}

@Composable
private fun InvertedPanel(
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit,
) {
    Column(
        modifier =
            modifier
                .background(
                    color = Color(0xFF080808),
                    shape = RoundedCornerShape(32.dp),
                ).border(
                    width = 1.dp,
                    color = Color(0xFF2A2A2A),
                    shape = RoundedCornerShape(32.dp),
                ).padding(22.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        content = content,
    )
}

@Composable
private fun InvertedMetricRow(
    label: String,
    value: String,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        BasicText(
            text = label,
            modifier = Modifier.weight(1f),
            style =
                TextStyle(
                    color = Color(0xFFAFAFAF),
                    fontSize = 14.sp,
                ),
        )
        BasicText(
            text = value,
            style =
                TextStyle(
                    color = Color.White,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                ),
        )
    }
}

@Composable
private fun PanelSectionTitle(
    title: String,
    description: String,
) {
    LabelBlock(
        label = title,
        description = description,
        modifier = Modifier.padding(vertical = 16.dp),
    )
}

@Composable
private fun ComponentPanel(
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit,
) {
    Column(
        modifier =
            modifier
                .background(
                    color = Color(0xF7FFFFFF),
                    shape = RoundedCornerShape(28.dp),
                ).border(
                    width = 1.dp,
                    color = Color(0x1F000000),
                    shape = RoundedCornerShape(28.dp),
                ).padding(horizontal = 18.dp, vertical = 8.dp),
        content = content,
    )
}

@Composable
private fun StatusRow(
    label: String,
    value: String,
) {
    Row(
        modifier =
            Modifier
                .fillMaxWidth()
                .padding(vertical = 14.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        BasicText(
            text = label,
            modifier = Modifier.weight(1f),
            style =
                TextStyle(
                    color = Color(0xFF505050),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                ),
        )
        BasicText(
            text = value,
            style =
                TextStyle(
                    color = Color(0xFF070707),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                ),
        )
    }
}

@Composable
private fun ShowcaseGalleryRow(
    entry: ShowcaseEntry,
    onClick: () -> Unit,
) {
    Row(
        modifier =
            Modifier
                .fillMaxWidth()
                .clickable(onClick = onClick)
                .padding(vertical = 14.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        LabelBlock(
            label = entry.title,
            description = entry.summary,
            modifier = Modifier.weight(1f),
        )
        BasicText(
            text = "Open",
            style =
                TextStyle(
                    color = Color(0xFF1B6BF2),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                ),
        )
    }
}

@Composable
private fun PanelDivider() {
    Spacer(
        modifier =
            Modifier
                .fillMaxWidth()
                .height(1.dp)
                .background(Color(0x1F000000)),
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
        modifier =
            Modifier
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
        modifier =
            Modifier
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
                style =
                    TextStyle(
                        color = Color(0xFF070707),
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
        modifier =
            Modifier
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
            style =
                TextStyle(
                    color = Color(0xFF070707),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                ),
        )
        BasicText(
            text = description,
            style =
                TextStyle(
                    color = Color(0xFF555555),
                    fontSize = 13.sp,
                    lineHeight = 18.sp,
                ),
        )
    }
}

private fun sampleBackground(): Brush =
    Brush.linearGradient(
        colors =
            listOf(
                Color(0xFFF7F7F7),
                Color(0xFFEDEDED),
                Color(0xFFDADADA),
            ),
    )
