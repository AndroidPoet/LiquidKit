@file:OptIn(ExperimentalForeignApi::class, ExperimentalComposeUiApi::class)

package io.github.androidpoet.liquidkit.surface

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.isSpecified
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.viewinterop.UIKitInteropProperties
import androidx.compose.ui.viewinterop.UIKitView
import io.github.androidpoet.liquidkit.LiquidGlassStyle
import io.github.androidpoet.liquidkit.internal.toUIColor
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.useContents
import platform.Foundation.NSProcessInfo
import platform.UIKit.UIBlurEffect
import platform.UIKit.UIBlurEffectStyle
import platform.UIKit.UIGlassEffect
import platform.UIKit.UIGlassEffectStyle
import platform.UIKit.UIVisualEffect
import platform.UIKit.UIVisualEffectView

/**
 * Renders a genuine native iOS glass surface using a [UIVisualEffectView].
 *
 * Prefers the iOS 26 `UIGlassEffect` (with tint + interactivity); on earlier systems it falls back
 * to a `UIBlurEffect` system material. The Android refraction shader is never used here.
 */
@Composable
internal actual fun PlatformLiquidSurface(
    modifier: Modifier,
    shape: Shape,
    cornerRadius: Dp,
    tint: Color,
    interactive: Boolean,
    style: LiquidGlassStyle,
    content: @Composable BoxScope.() -> Unit,
) {
    val cornerPx = cornerRadius.value.toDouble()
    val effect = remember(tint, interactive) { makeGlassVisualEffect(tint, interactive) }

    Box(modifier = modifier) {
        UIKitView(
            factory = {
                UIVisualEffectView(effect = effect).apply {
                    layer.cornerRadius = cornerPx
                    layer.masksToBounds = true
                    setUserInteractionEnabled(false)
                }
            },
            modifier = Modifier.fillMaxSize(),
            update = { view ->
                view.effect = effect
                view.layer.cornerRadius = cornerPx
            },
            onRelease = {},
            properties = UIKitInteropProperties(
                // Native glass sits behind Compose content; it must never intercept touches.
                interactionMode = null,
                isNativeAccessibilityEnabled = false,
            ),
        )
        // Compose content drawn on top of the native glass material.
        content()
    }
}

@Composable
internal actual fun PlatformLiquidGlassContainer(
    modifier: Modifier,
    content: @Composable BoxScope.() -> Unit,
) {
    // On iOS each LiquidSurface hosts its own native material, so the container is a plain group.
    Box(modifier = modifier, content = content)
}

private fun makeGlassVisualEffect(tint: Color, interactive: Boolean): UIVisualEffect {
    if (isiOS26OrLater()) {
        val glass = UIGlassEffect.effectWithStyle(UIGlassEffectStyle.UIGlassEffectStyleRegular)
        glass.interactive = interactive
        if (tint.isSpecified) {
            glass.tintColor = tint.toUIColor()
        }
        return glass
    }
    // Fallback for pre-iOS 26 systems where UIGlassEffect is unavailable.
    return UIBlurEffect.effectWithStyle(UIBlurEffectStyle.UIBlurEffectStyleSystemMaterial)
}

private fun isiOS26OrLater(): Boolean =
    NSProcessInfo.processInfo.operatingSystemVersion.useContents { majorVersion >= 26L }
