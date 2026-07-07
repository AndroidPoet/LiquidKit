package io.github.androidpoet.liquidkit.dropdown

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import io.github.androidpoet.liquidkit.LiquidGlassStyle
import io.github.androidpoet.liquidkit.LocalLiquidGlassStyle
import io.github.androidpoet.liquidkit.icon.LiquidIcon

/**
 * A single selectable entry in a [LiquidMenu] / [LiquidDropdownMenu].
 *
 * @param key Stable identity returned through `onSelect`.
 * @param label Text shown for the item.
 * @param icon Optional leading icon (Compose vector on Android, SF Symbol on iOS).
 * @param enabled When false the item is shown dimmed and is not selectable.
 */
public data class LiquidMenuItem<T : Any>(
    public val key: T,
    public val label: String,
    public val icon: LiquidIcon? = null,
    public val enabled: Boolean = true,
)

/**
 * A glass dropdown menu presented from an anchor.
 *
 * On Android the popup container is rendered as a Liquid Glass surface. On iOS it
 * uses a genuine native `UIButton` with `showsMenuAsPrimaryAction` + `UIMenu` so it
 * picks up authentic system Liquid Glass on iOS 26.
 *
 * @param items The selectable entries.
 * @param onSelect Invoked with the [LiquidMenuItem.key] of the chosen entry.
 * @param modifier Modifier applied to the anchor.
 * @param label Text shown on the anchor button.
 * @param selectedKey Optional currently-selected key, used to show a checkmark / state.
 * @param enabled Whether the whole control is interactive.
 * @param style Glass style tokens.
 */
@Composable
public fun <T : Any> LiquidDropdownMenu(
    items: List<LiquidMenuItem<T>>,
    onSelect: (T) -> Unit,
    modifier: Modifier = Modifier,
    label: String = "Menu",
    selectedKey: T? = null,
    enabled: Boolean = true,
    style: LiquidGlassStyle = LocalLiquidGlassStyle.current,
) {
    require(items.isNotEmpty()) { "LiquidDropdownMenu requires at least one item." }

    PlatformLiquidDropdownMenu(
        items = items,
        onSelect = onSelect,
        modifier = modifier,
        label = label,
        selectedKey = selectedKey,
        enabled = enabled,
        style = style,
    )
}

/** Alias for [LiquidDropdownMenu], matching the SwiftUI `Menu` naming. */
@Composable
public fun <T : Any> LiquidMenu(
    items: List<LiquidMenuItem<T>>,
    onSelect: (T) -> Unit,
    modifier: Modifier = Modifier,
    label: String = "Menu",
    selectedKey: T? = null,
    enabled: Boolean = true,
    style: LiquidGlassStyle = LocalLiquidGlassStyle.current,
): Unit =
    LiquidDropdownMenu(
        items = items,
        onSelect = onSelect,
        modifier = modifier,
        label = label,
        selectedKey = selectedKey,
        enabled = enabled,
        style = style,
    )

@Composable
internal expect fun <T : Any> PlatformLiquidDropdownMenu(
    items: List<LiquidMenuItem<T>>,
    onSelect: (T) -> Unit,
    modifier: Modifier,
    label: String,
    selectedKey: T?,
    enabled: Boolean,
    style: LiquidGlassStyle,
)
