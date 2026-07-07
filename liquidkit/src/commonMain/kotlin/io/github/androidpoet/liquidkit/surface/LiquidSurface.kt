package io.github.androidpoet.liquidkit.surface

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import io.github.androidpoet.liquidkit.LiquidGlassStyle
import io.github.androidpoet.liquidkit.LocalLiquidGlassStyle

/**
 * A Liquid Glass card / container that renders genuine native glass behind arbitrary [content].
 *
 * This is the cross-platform counterpart of iOS 26's `GlassEffectContainer` /
 * `.glassEffect()` applied to a custom view:
 *
 * - On **Android** the [io.github.androidpoet.liquidkit] backdrop engine samples the content drawn
 *   behind the surface and applies refraction, blur, highlight and shadow (Kyant-style Liquid Glass).
 * - On **iOS** a native `UIVisualEffectView` configured with the iOS 26 `UIGlassEffect`
 *   (falling back to a system blur material when `UIGlassEffect` is unavailable) is hosted via
 *   Compose `UIKitView` interop. The Android shader is never used on iOS.
 *
 * @param modifier Sizing / placement modifier applied to the surface.
 * @param shape The clipping / refraction shape of the glass. Defaults to a rounded card.
 * @param tint Optional color wash blended into the glass. [Color.Unspecified] applies no tint.
 * @param interactive When `true` the surface gains the engine's interactive press / drag highlight
 *   on Android (a no-op on iOS, where the native material owns interaction).
 * @param style The [LiquidGlassStyle] supplying corner radius and default tint when [shape] / [tint]
 *   are not overridden.
 * @param content Content drawn on top of the glass.
 */
@Composable
public fun LiquidSurface(
    modifier: Modifier = Modifier,
    shape: Shape? = null,
    tint: Color = Color.Unspecified,
    interactive: Boolean = false,
    style: LiquidGlassStyle = LocalLiquidGlassStyle.current,
    content: @Composable BoxScope.() -> Unit,
) {
    val resolvedShape = shape ?: RoundedCornerShape(style.cornerRadius)
    PlatformLiquidSurface(
        modifier = modifier,
        shape = resolvedShape,
        cornerRadius = style.cornerRadius,
        tint = tint,
        interactive = interactive,
        style = style,
        content = content,
    )
}

@Composable
internal expect fun PlatformLiquidSurface(
    modifier: Modifier,
    shape: Shape,
    cornerRadius: Dp,
    tint: Color,
    interactive: Boolean,
    style: LiquidGlassStyle,
    content: @Composable BoxScope.() -> Unit,
)

/**
 * Groups multiple [LiquidSurface] (or other glass) children so that, on Android, they sample a
 * single shared backdrop region — the morphing / coherence idea behind iOS 26's
 * `GlassEffectContainer`.
 *
 * The container itself is a transparent [Box]; it captures the content drawn behind it once and
 * exposes that shared sampling layer to nested [LiquidSurface] children. On iOS each child hosts
 * its own native material, so this is a plain grouping layout there.
 *
 * @param modifier Layout modifier for the group.
 * @param content The glass children, typically one or more [LiquidSurface].
 */
@Composable
public fun LiquidGlassContainer(
    modifier: Modifier = Modifier,
    content: @Composable BoxScope.() -> Unit,
) {
    PlatformLiquidGlassContainer(
        modifier = modifier,
        content = content,
    )
}

@Composable
internal expect fun PlatformLiquidGlassContainer(
    modifier: Modifier,
    content: @Composable BoxScope.() -> Unit,
)
