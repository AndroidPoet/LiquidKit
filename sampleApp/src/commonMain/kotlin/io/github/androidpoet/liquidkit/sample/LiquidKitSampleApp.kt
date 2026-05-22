package io.github.androidpoet.liquidkit.sample

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
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
import io.github.androidpoet.liquidkit.icon.LiquidIcon
import io.github.androidpoet.liquidkit.navigation.LiquidNavigationScaffold
import io.github.androidpoet.liquidkit.navigation.LiquidNavigationItem
import io.github.androidpoet.liquidkit.toggle.LiquidToggle

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
    val navigationItems = rememberLiquidKitNavigationItems()

    LiquidNavigationScaffold(
        items = navigationItems,
        modifier = modifier
            .fillMaxSize()
            .background(sampleBackground()),
        navigationModifier = Modifier
            .navigationBarsPadding()
            .padding(16.dp),
    ) { selectedTab ->
        LiquidKitSampleTabContent(selectedTab)
    }
}

@Composable
public fun LiquidKitSampleTabContent(
    selectedTab: LiquidKitSampleTab,
    modifier: Modifier = Modifier,
) {
    var notificationsEnabled by remember { mutableStateOf(true) }
    var compactModeEnabled by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 24.dp, vertical = 32.dp),
        verticalArrangement = Arrangement.spacedBy(18.dp),
    ) {
        Header(selectedTab)

        ToggleRow(
            label = "Notifications",
            description = "Rendered by AndroidLiquidGlass on Android and UISwitch on iOS.",
            value = notificationsEnabled,
            onValueChange = { notificationsEnabled = it },
        )
        ToggleRow(
            label = "Compact mode",
            description = "Same common API, platform-native renderer underneath.",
            value = compactModeEnabled,
            onValueChange = { compactModeEnabled = it },
        )

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

@Composable
private fun rememberLiquidKitNavigationItems(): List<LiquidNavigationItem<LiquidKitSampleTab>> =
    remember {
        listOf(
            LiquidNavigationItem(
                key = LiquidKitSampleTab.Home,
                label = LiquidKitSampleTab.Home.title,
                icon = LiquidIcon(
                    imageVector = SampleIcons.Home,
                    contentDescription = LiquidKitSampleTab.Home.title,
                    iosSystemName = LiquidKitSampleTab.Home.iosSystemImage,
                    selectedIosSystemName = "house.fill",
                ),
            ),
            LiquidNavigationItem(
                key = LiquidKitSampleTab.Search,
                label = LiquidKitSampleTab.Search.title,
                icon = LiquidIcon(
                    imageVector = SampleIcons.Controls,
                    contentDescription = LiquidKitSampleTab.Search.title,
                    iosSystemName = LiquidKitSampleTab.Search.iosSystemImage,
                ),
            ),
            LiquidNavigationItem(
                key = LiquidKitSampleTab.Settings,
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
private fun Header(selectedTab: LiquidKitSampleTab) {
    val subtitle = when (selectedTab) {
        LiquidKitSampleTab.Home -> "Bottom navigation and toggle only."
        LiquidKitSampleTab.Search -> "Android uses the vendored AndroidLiquidGlass bottom tabs."
        LiquidKitSampleTab.Settings -> "iOS uses native TabView, NavigationStack, and UISwitch controls."
    }

    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
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
private fun ToggleRow(
    label: String,
    description: String,
    value: Boolean,
    onValueChange: (Boolean) -> Unit,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column(
            modifier = Modifier.weight(1f),
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
        LiquidToggle(
            checked = value,
            onCheckedChange = onValueChange,
            modifier = Modifier.size(width = 62.dp, height = 36.dp),
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
