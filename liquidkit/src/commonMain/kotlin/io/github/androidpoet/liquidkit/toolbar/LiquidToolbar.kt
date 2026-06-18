package io.github.androidpoet.liquidkit.toolbar

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import io.github.androidpoet.liquidkit.LiquidGlassStyle

/**
 * A Liquid Glass top app bar (toolbar / navigation bar).
 *
 * This is the Compose equivalent of iOS 26's auto-glass navigation bar: a glass
 * surface that hosts a [title] alongside optional [leading] and [trailing] action
 * slots.
 *
 * On Android the bar is rendered as a real glass surface backed by the LiquidKit
 * backdrop engine. On iOS it is rendered with a genuine native [platform.UIKit.UINavigationBar]
 * which automatically adopts Liquid Glass on iOS 26.
 *
 * @param title slot for the bar's title content (rendered centered/leading per platform).
 * @param modifier applied to the bar container.
 * @param leading optional slot for a leading action (e.g. a back button).
 * @param trailing optional slot for a trailing action (e.g. an overflow / done button).
 * @param style glass styling. Defaults to [LiquidGlassStyle.NavigationBar].
 */
@Composable
public fun LiquidToolbar(
    title: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    leading: (@Composable () -> Unit)? = null,
    trailing: (@Composable () -> Unit)? = null,
    style: LiquidGlassStyle = LiquidGlassStyle.NavigationBar,
) {
    PlatformLiquidToolbar(
        title = title,
        modifier = modifier,
        leading = leading,
        trailing = trailing,
        style = style,
    )
}

@Composable
internal expect fun PlatformLiquidToolbar(
    title: @Composable () -> Unit,
    modifier: Modifier,
    leading: (@Composable () -> Unit)?,
    trailing: (@Composable () -> Unit)?,
    style: LiquidGlassStyle,
)
