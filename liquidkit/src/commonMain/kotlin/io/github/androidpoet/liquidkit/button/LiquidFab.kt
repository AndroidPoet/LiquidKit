package io.github.androidpoet.liquidkit.button

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import io.github.androidpoet.liquidkit.LiquidGlassStyle
import io.github.androidpoet.liquidkit.LocalLiquidGlassStyle
import io.github.androidpoet.liquidkit.icon.LiquidIcon

/**
 * A circular Liquid Glass floating action button.
 *
 * On Android it is rendered with the LiquidKit backdrop shader engine; on iOS it is a genuine
 * native [platform.UIKit.UIButton] configured with the system Liquid Glass configuration.
 *
 * @param icon symbol shown at the center of the FAB.
 * @param onClick invoked when the FAB is tapped.
 * @param variant [LiquidButtonVariant.Glass] (translucent) or
 *   [LiquidButtonVariant.GlassProminent] (opaque, primary).
 */
@Composable
public fun LiquidFab(
    icon: LiquidIcon,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    variant: LiquidButtonVariant = LiquidButtonVariant.GlassProminent,
    enabled: Boolean = true,
    style: LiquidGlassStyle = LocalLiquidGlassStyle.current,
) {
    PlatformLiquidFab(
        icon = icon,
        onClick = onClick,
        modifier = modifier,
        variant = variant,
        enabled = enabled,
        style = style,
    )
}

@Composable
internal expect fun PlatformLiquidFab(
    icon: LiquidIcon,
    onClick: () -> Unit,
    modifier: Modifier,
    variant: LiquidButtonVariant,
    enabled: Boolean,
    style: LiquidGlassStyle,
)
