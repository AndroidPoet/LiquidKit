package io.github.androidpoet.liquidkit.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import io.github.androidpoet.liquidkit.LiquidGlassStyle

@Composable
public fun <T : Any> LiquidBottomNavigation(
    items: List<LiquidNavigationItem<T>>,
    selectedKey: T,
    onSelected: (T) -> Unit,
    modifier: Modifier = Modifier,
    style: LiquidGlassStyle = LiquidGlassStyle.NavigationBar,
) {
    require(items.isNotEmpty()) { "LiquidBottomNavigation requires at least one item." }

    LiquidBottomNavigationPlatform(
        items = items,
        selectedKey = selectedKey,
        onSelected = onSelected,
        modifier = modifier,
        style = style,
    )
}

@Composable
internal expect fun <T : Any> LiquidBottomNavigationPlatform(
    items: List<LiquidNavigationItem<T>>,
    selectedKey: T,
    onSelected: (T) -> Unit,
    modifier: Modifier,
    style: LiquidGlassStyle,
)
