package io.github.androidpoet.liquidkit.sample

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.displayCutoutPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.BasicText
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.WindowCompat
import io.github.androidpoet.liquidkit.internal.androidglass.backdrop.backdrops.LayerBackdrop
import io.github.androidpoet.liquidkit.internal.androidglass.backdrop.backdrops.layerBackdrop
import io.github.androidpoet.liquidkit.internal.androidglass.backdrop.backdrops.rememberCanvasBackdrop
import io.github.androidpoet.liquidkit.internal.androidglass.backdrop.backdrops.rememberLayerBackdrop
import io.github.androidpoet.liquidkit.internal.androidglass.catalog.components.LiquidBottomTab
import io.github.androidpoet.liquidkit.internal.androidglass.catalog.components.LiquidBottomTabs
import io.github.androidpoet.liquidkit.internal.androidglass.catalog.components.LiquidButton
import io.github.androidpoet.liquidkit.internal.androidglass.catalog.components.LiquidSlider
import io.github.androidpoet.liquidkit.internal.androidglass.catalog.components.LiquidToggle

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContent { GlassCatalogApp() }
    }
}

private enum class Screen { Home, Buttons, Controls, BottomTabs }

@Composable
private fun GlassCatalogApp() {
    var screen by rememberSaveable { mutableStateOf(Screen.Home) }
    BackHandler(screen != Screen.Home) { screen = Screen.Home }

    when (screen) {
        Screen.Home -> HomeScreen(onNavigate = { screen = it })
        Screen.Buttons -> ButtonsScreen()
        Screen.Controls -> ControlsScreen()
        Screen.BottomTabs -> BottomTabsScreen()
    }
}

// ── Scaffold ─────────────────────────────────────────────────────────────────

@Composable
private fun DemoScaffold(
    wallpaper: Int = R.drawable.wallpaper_light,
    content: @Composable BoxScope.(backdrop: LayerBackdrop) -> Unit,
) {
    val backdrop = rememberLayerBackdrop()

    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Image(
            painter = painterResource(wallpaper),
            contentDescription = null,
            modifier =
                Modifier
                    .layerBackdrop(backdrop)
                    .fillMaxSize(),
            contentScale = ContentScale.Crop,
        )
        content(backdrop)
    }
}

// ── Home ──────────────────────────────────────────────────────────────────────

@Composable
private fun HomeScreen(onNavigate: (Screen) -> Unit) {
    val isLight = !isSystemInDarkTheme()
    val fg = if (isLight) Color.Black else Color.White

    DemoScaffold {
        Column(
            Modifier
                .verticalScroll(rememberScrollState())
                .systemBarsPadding()
                .displayCutoutPadding()
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(0.dp),
        ) {
            BasicText(
                "Glass Catalog",
                Modifier.padding(16.dp, 40.dp, 16.dp, 8.dp),
                style = TextStyle(fg, 28.sp, FontWeight.SemiBold),
            )
            BasicText(
                "Kyant-style liquid glass components on Android",
                Modifier.padding(horizontal = 16.dp).padding(bottom = 24.dp),
                style = TextStyle(fg.copy(0.5f), 14.sp),
            )

            SectionHeader("Components")
            NavRow("Buttons", fg) { onNavigate(Screen.Buttons) }
            NavRow("Toggle & Slider", fg) { onNavigate(Screen.Controls) }
            NavRow("Bottom tabs", fg) { onNavigate(Screen.BottomTabs) }
        }
    }
}

@Composable
private fun SectionHeader(label: String) {
    BasicText(
        label,
        Modifier.padding(16.dp, 20.dp, 16.dp, 6.dp).fillMaxWidth(),
        style = TextStyle(Color(0xFF0088FF), 13.sp, FontWeight.SemiBold),
    )
}

@Composable
private fun NavRow(label: String, fg: Color, onClick: () -> Unit) {
    Row(
        Modifier
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 14.dp)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        BasicText(label, style = TextStyle(fg, 17.sp))
        BasicText("›", style = TextStyle(fg.copy(0.4f), 20.sp))
    }
    Box(
        Modifier
            .padding(horizontal = 16.dp)
            .fillMaxWidth()
            .height(0.5.dp)
            .background(fg.copy(0.08f)),
    )
}

// ── Buttons ───────────────────────────────────────────────────────────────────

@Composable
private fun ButtonsScreen() {
    DemoScaffold { backdrop ->
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            LiquidButton({}, backdrop) {
                BasicText("Transparent Liquid Button", style = TextStyle(Color.Black, 15.sp))
            }
            LiquidButton({}, backdrop, surfaceColor = Color.White.copy(0.3f)) {
                BasicText("Surface Liquid Button", style = TextStyle(Color.Black, 15.sp))
            }
            LiquidButton({}, backdrop, tint = Color(0xFF0088FF)) {
                BasicText("Blue Tinted Button", style = TextStyle(Color.White, 15.sp))
            }
            LiquidButton({}, backdrop, tint = Color(0xFFFF8D28)) {
                BasicText("Orange Tinted Button", style = TextStyle(Color.White, 15.sp))
            }
            LiquidButton({}, backdrop, tint = Color(0xFF34C759)) {
                BasicText("Green Tinted Button", style = TextStyle(Color.White, 15.sp))
            }
        }
    }
}

// ── Controls ──────────────────────────────────────────────────────────────────

@Composable
private fun ControlsScreen() {
    val isLight = !isSystemInDarkTheme()
    val cardBg = if (isLight) Color.White else Color(0xFF1C1C1E)

    DemoScaffold { backdrop ->
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(32.dp),
            modifier = Modifier.padding(horizontal = 32.dp),
        ) {
            // Toggle directly over wallpaper
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                BasicText("Toggle", style = TextStyle(Color.White, 13.sp, FontWeight.Medium))
                var tog1 by rememberSaveable { mutableStateOf(false) }
                LiquidToggle({ tog1 }, { tog1 = it }, backdrop)

                // Toggle over card (canvas backdrop)
                var tog2 by rememberSaveable { mutableStateOf(true) }
                Box(
                    Modifier
                        .background(
                            cardBg,
                            androidx.compose.foundation.shape
                                .RoundedCornerShape(20.dp),
                        ).padding(20.dp),
                ) {
                    LiquidToggle({ tog2 }, { tog2 = it }, rememberCanvasBackdrop { drawRect(cardBg) })
                }
            }

            // Slider
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.fillMaxWidth(),
            ) {
                BasicText("Slider", style = TextStyle(Color.White, 13.sp, FontWeight.Medium))
                var value by rememberSaveable { mutableStateOf(0.42f) }
                LiquidSlider(
                    value = { value },
                    onValueChange = { value = it },
                    valueRange = 0f..1f,
                    visibilityThreshold = 0.001f,
                    backdrop = backdrop,
                    modifier = Modifier.fillMaxWidth(),
                )
            }
        }
    }
}

// ── Bottom Tabs ───────────────────────────────────────────────────────────────

@Composable
private fun BottomTabsScreen() {
    val isLight = !isSystemInDarkTheme()
    val fg = if (isLight) Color.Black else Color.White
    val icon = painterResource(R.drawable.flight_40px)
    val iconFilter = ColorFilter.tint(fg)

    DemoScaffold(wallpaper = R.drawable.system_home_screen_light) { backdrop ->
        Column(
            Modifier.navigationBarsPadding().fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(40.dp, Alignment.CenterVertically),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            // 3 tabs
            var tab3 by rememberSaveable { mutableIntStateOf(0) }
            LiquidBottomTabs(
                selectedTabIndex = { tab3 },
                onTabSelected = { tab3 = it },
                backdrop = backdrop,
                tabsCount = 3,
                modifier = Modifier.padding(horizontal = 36.dp),
            ) {
                repeat(3) { i ->
                    LiquidBottomTab({ tab3 = i }) {
                        Box(Modifier.size(28.dp).paint(icon, colorFilter = iconFilter))
                        BasicText("Tab ${i + 1}", style = TextStyle(fg, 12.sp))
                    }
                }
            }

            // 4 tabs
            var tab4 by rememberSaveable { mutableIntStateOf(1) }
            LiquidBottomTabs(
                selectedTabIndex = { tab4 },
                onTabSelected = { tab4 = it },
                backdrop = backdrop,
                tabsCount = 4,
                modifier = Modifier.padding(horizontal = 24.dp),
            ) {
                repeat(4) { i ->
                    LiquidBottomTab({ tab4 = i }) {
                        Box(Modifier.size(28.dp).paint(icon, colorFilter = iconFilter))
                        BasicText("Tab ${i + 1}", style = TextStyle(fg, 12.sp))
                    }
                }
            }

            // 5 tabs
            var tab5 by rememberSaveable { mutableIntStateOf(2) }
            LiquidBottomTabs(
                selectedTabIndex = { tab5 },
                onTabSelected = { tab5 = it },
                backdrop = backdrop,
                tabsCount = 5,
                modifier = Modifier.padding(horizontal = 8.dp),
            ) {
                repeat(5) { i ->
                    LiquidBottomTab({ tab5 = i }) {
                        Box(Modifier.size(24.dp).paint(icon, colorFilter = iconFilter))
                        BasicText("${i + 1}", style = TextStyle(fg, 11.sp))
                    }
                }
            }
        }
    }
}
