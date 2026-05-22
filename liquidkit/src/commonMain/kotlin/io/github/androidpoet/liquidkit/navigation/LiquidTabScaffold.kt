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
public class LiquidTabState<T : Any> internal constructor(initialKey: T) {
    public var selectedKey: T by mutableStateOf(initialKey)
        private set

    public fun select(key: T) {
        selectedKey = key
    }
}

@Composable
public fun <T : Any> rememberLiquidTabState(
    initialKey: T,
): LiquidTabState<T> = remember(initialKey) {
    LiquidTabState(initialKey)
}

@Composable
public fun <T : Any> LiquidTabScaffold(
    items: List<LiquidNavigationItem<T>>,
    modifier: Modifier = Modifier,
    navigationModifier: Modifier = Modifier,
    style: LiquidGlassStyle = LiquidGlassStyle.NavigationBar,
    onSelected: (T) -> Unit = {},
    content: @Composable BoxScope.(selectedKey: T) -> Unit,
) {
    require(items.isNotEmpty()) { "LiquidTabScaffold requires at least one item." }

    val tabState = rememberLiquidTabState(items.first().key)

    LiquidTabScaffold(
        items = items,
        state = tabState,
        modifier = modifier,
        navigationModifier = navigationModifier,
        style = style,
        onSelected = onSelected,
        content = content,
    )
}

@Composable
public fun <T : Any> LiquidTabScaffold(
    items: List<LiquidNavigationItem<T>>,
    state: LiquidTabState<T>,
    modifier: Modifier = Modifier,
    navigationModifier: Modifier = Modifier,
    style: LiquidGlassStyle = LiquidGlassStyle.NavigationBar,
    onSelected: (T) -> Unit = {},
    content: @Composable BoxScope.(selectedKey: T) -> Unit,
) {
    require(items.isNotEmpty()) { "LiquidTabScaffold requires at least one item." }

    Box(modifier = modifier) {
        content(state.selectedKey)
        LiquidBottomNavigation(
            items = items,
            selectedKey = state.selectedKey,
            onSelected = { key ->
                state.select(key)
                onSelected(key)
            },
            modifier = navigationModifier.align(Alignment.BottomCenter),
            style = style,
        )
    }
}
