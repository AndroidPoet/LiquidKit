package io.github.androidpoet.liquidkit.button

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import io.github.androidpoet.liquidkit.LiquidGlassStyle
import io.github.androidpoet.liquidkit.LocalLiquidGlassStyle

/**
 * Visual variants for [LiquidButton], mirroring the iOS 26 Liquid Glass button styles.
 */
public enum class LiquidButtonVariant {
    /** Translucent, secondary-emphasis glass (iOS `UIButton.Configuration.glass()`). */
    Glass,

    /** Opaque, primary-emphasis glass (iOS `UIButton.Configuration.prominentGlass()`). */
    GlassProminent,
}

/**
 * A Liquid Glass button.
 *
 * On Android it is rendered with the LiquidKit backdrop shader engine; on iOS it is a genuine
 * native [platform.UIKit.UIButton] configured with the system Liquid Glass configuration so it
 * picks up authentic system rendering.
 *
 * @param text label shown inside the button.
 * @param onClick invoked when the button is tapped.
 * @param variant [LiquidButtonVariant.Glass] (secondary/translucent) or
 *   [LiquidButtonVariant.GlassProminent] (primary/opaque).
 */
@Composable
public fun LiquidButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    variant: LiquidButtonVariant = LiquidButtonVariant.Glass,
    enabled: Boolean = true,
    style: LiquidGlassStyle = LocalLiquidGlassStyle.current,
) {
    PlatformLiquidButton(
        text = text,
        onClick = onClick,
        modifier = modifier,
        variant = variant,
        enabled = enabled,
        style = style,
    )
}

@Composable
internal expect fun PlatformLiquidButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier,
    variant: LiquidButtonVariant,
    enabled: Boolean,
    style: LiquidGlassStyle,
)
