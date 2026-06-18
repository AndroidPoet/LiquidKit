package io.github.androidpoet.liquidkit.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import io.github.androidpoet.liquidkit.LiquidGlassStyle
import io.github.androidpoet.liquidkit.internal.InternalLiquidKitApi
import io.github.androidpoet.liquidkit.internal.LocalLiquidLayerBackdrop
import io.github.androidpoet.liquidkit.internal.captureToLiquidLayerBackdrop
import io.github.androidpoet.liquidkit.internal.rememberLiquidLayerBackdrop

@Stable
public class LiquidTabState<T : Any> internal constructor(
    initialKey: T,
) {
    public var selectedKey: T by mutableStateOf(initialKey)
        private set

    public fun select(key: T) {
        selectedKey = key
    }
}

@Composable
public fun <T : Any> rememberLiquidTabState(
    initialKey: T,
): LiquidTabState<T> =
    remember(initialKey) {
        LiquidTabState(initialKey)
    }

@Composable
public fun <T : Any> LiquidTabScaffold(
    items: List<LiquidNavigationItem<T>>,
    modifier: Modifier = Modifier,
    navigationModifier: Modifier = Modifier,
    style: LiquidGlassStyle = LiquidGlassStyle.NavigationBar,
    onSelected: (T) -> Unit = {},
    bottomNavigation: @Composable BoxScope.(
        selectedKey: T,
        onSelected: (T) -> Unit,
    ) -> Unit = { selectedKey, onTabSelected ->
        LiquidBottomNavigation(
            items = items,
            selectedKey = selectedKey,
            onSelected = onTabSelected,
            modifier = navigationModifier.align(Alignment.BottomCenter),
            style = style,
        )
    },
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
        bottomNavigation = bottomNavigation,
        content = content,
    )
}

@OptIn(InternalLiquidKitApi::class)
@Composable
public fun <T : Any> LiquidTabScaffold(
    items: List<LiquidNavigationItem<T>>,
    state: LiquidTabState<T>,
    modifier: Modifier = Modifier,
    navigationModifier: Modifier = Modifier,
    style: LiquidGlassStyle = LiquidGlassStyle.NavigationBar,
    onSelected: (T) -> Unit = {},
    bottomNavigation: @Composable BoxScope.(
        selectedKey: T,
        onSelected: (T) -> Unit,
    ) -> Unit = { selectedKey, onTabSelected ->
        LiquidBottomNavigation(
            items = items,
            selectedKey = selectedKey,
            onSelected = onTabSelected,
            modifier = navigationModifier.align(Alignment.BottomCenter),
            style = style,
        )
    },
    content: @Composable BoxScope.(selectedKey: T) -> Unit,
) {
    require(items.isNotEmpty()) { "LiquidTabScaffold requires at least one item." }

    val layerBackdrop = rememberLiquidLayerBackdrop()
    Box(modifier = modifier) {
        // Content records into the layer — must NOT read from it or a circular GraphicsLayer
        // reference forms (content-capture layer → toggle inside content → same layer → ∞).
        Box(Modifier.matchParentSize().captureToLiquidLayerBackdrop(layerBackdrop)) {
            content(state.selectedKey)
        }
        // Nav bar is scoped to the layer so it can blur the content behind it.
        CompositionLocalProvider(LocalLiquidLayerBackdrop provides layerBackdrop) {
            bottomNavigation(state.selectedKey) { key ->
                state.select(key)
                onSelected(key)
            }
        }
    }
}
