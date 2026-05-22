package io.github.androidpoet.liquidkit.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import io.github.androidpoet.liquidkit.LiquidGlassStyle

@Stable
public class LiquidNavigationState<T : Any> internal constructor(initialKey: T) {
    public var selectedKey: T by mutableStateOf(initialKey)
        private set

    public fun navigate(key: T) {
        selectedKey = key
    }
}

@Composable
public fun <T : Any> rememberLiquidNavigationState(
    initialKey: T,
): LiquidNavigationState<T> = remember(initialKey) {
    LiquidNavigationState(initialKey)
}

@Composable
public fun <T : Any> LiquidNavigationScaffold(
    items: List<LiquidNavigationItem<T>>,
    modifier: Modifier = Modifier,
    navigationModifier: Modifier = Modifier,
    style: LiquidGlassStyle = LiquidGlassStyle.NavigationBar,
    onSelected: (T) -> Unit = {},
    content: @Composable BoxScope.(selectedKey: T) -> Unit,
) {
    require(items.isNotEmpty()) { "LiquidNavigationScaffold requires at least one item." }

    val navigationState = rememberLiquidNavigationState(items.first().key)

    LiquidNavigationScaffold(
        items = items,
        state = navigationState,
        modifier = modifier,
        navigationModifier = navigationModifier,
        style = style,
        onSelected = onSelected,
        content = content,
    )
}

@Composable
public fun <T : Any> LiquidNavigationScaffold(
    items: List<LiquidNavigationItem<T>>,
    state: LiquidNavigationState<T>,
    modifier: Modifier = Modifier,
    navigationModifier: Modifier = Modifier,
    style: LiquidGlassStyle = LiquidGlassStyle.NavigationBar,
    onSelected: (T) -> Unit = {},
    content: @Composable BoxScope.(selectedKey: T) -> Unit,
) {
    require(items.isNotEmpty()) { "LiquidNavigationScaffold requires at least one item." }

    Box(modifier = modifier) {
        content(state.selectedKey)
        LiquidBottomNavigation(
            items = items,
            selectedKey = state.selectedKey,
            onSelected = { key ->
                state.navigate(key)
                onSelected(key)
            },
            modifier = navigationModifier.align(Alignment.BottomCenter),
            style = style,
        )
    }
}
