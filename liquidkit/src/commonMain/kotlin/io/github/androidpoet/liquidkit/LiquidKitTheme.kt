package io.github.androidpoet.liquidkit

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf

/**
 * Provides a [LiquidGlassStyle] down the composition so every glass component
 * can pick it up without an explicit parameter at each call site.
 *
 * Defaults to [LiquidGlassStyle.Control] (light).  Wrap your app or screen
 * in [LiquidKitTheme] to switch to the dark variant automatically.
 */
public val LocalLiquidGlassStyle: androidx.compose.runtime.ProvidableCompositionLocal<LiquidGlassStyle> =
    staticCompositionLocalOf { LiquidGlassStyle.Control }

/**
 * Sets the default [LiquidGlassStyle] for all glass components in [content].
 *
 * When [style] is omitted the theme auto-selects the appropriate light/dark
 * preset based on [isSystemInDarkTheme].
 *
 * @param style Override the resolved style. Pass a custom [liquidGlassStyle]
 *   or one of the [LiquidGlassStyle.Control] / [LiquidGlassStyle.ControlDark]
 *   etc. presets.
 */
@Composable
public fun LiquidKitTheme(
    style: LiquidGlassStyle = if (isSystemInDarkTheme()) LiquidGlassStyle.ControlDark else LiquidGlassStyle.Control,
    content: @Composable () -> Unit,
) {
    CompositionLocalProvider(LocalLiquidGlassStyle provides style) {
        content()
    }
}
