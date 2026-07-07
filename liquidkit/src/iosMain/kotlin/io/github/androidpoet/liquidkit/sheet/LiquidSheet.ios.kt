@file:OptIn(ExperimentalForeignApi::class)

package io.github.androidpoet.liquidkit.sheet

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.UIKitInteropProperties
import androidx.compose.ui.viewinterop.UIKitView
import io.github.androidpoet.liquidkit.LiquidGlassStyle
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
 * iOS [LiquidSheet] rendered as a native glass-backed surface using
 * [UIVisualEffectView]. On iOS 26 it uses the dedicated `UIGlassEffect` (the same
 * typed binding used by `LiquidSurface`); on earlier systems it falls back to a
 * system [UIBlurEffect] (`systemMaterial`).
 *
 * NOTE: native sheet *presentation* (`UISheetPresentationController` detents,
 * grabber, interactive dismiss) is owned by the SwiftUI host. This component is
 * the in-Compose glass surface, which is the right scope for a reusable component.
 */
@OptIn(ExperimentalComposeUiApi::class)
@Composable
internal actual fun PlatformLiquidSheet(
    visible: Boolean,
    onDismiss: () -> Unit,
    modifier: Modifier,
    style: LiquidGlassStyle,
    content: @Composable () -> Unit,
) {
    if (!visible) return

    val currentOnDismiss = rememberUpdatedState(onDismiss)
    val scrimInteraction = remember { MutableInteractionSource() }

    Box(
        modifier =
            Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.32f))
                .clickable(
                    interactionSource = scrimInteraction,
                    indication = null,
                ) { currentOnDismiss.value() },
        contentAlignment = Alignment.BottomCenter,
    ) {
        Box(
            modifier =
                modifier
                    .fillMaxWidth()
                    .widthIn(max = 640.dp),
            contentAlignment = Alignment.TopCenter,
        ) {
            // Native glass background behind the Compose content.
            UIKitView(
                factory = {
                    UIVisualEffectView(effect = liquidGlassEffect()).apply {
                        clipsToBounds = true
                        layer.cornerRadius = style.cornerRadius.value.toDouble()
                    }
                },
                modifier = Modifier.fillMaxSize(),
                update = { view ->
                    view.layer.cornerRadius = style.cornerRadius.value.toDouble()
                },
                onRelease = {},
                properties =
                    UIKitInteropProperties(
                        interactionMode = null,
                        isNativeAccessibilityEnabled = false,
                        placedAsOverlay = false,
                    ),
            )

            // Compose content on top of the glass.
            Box(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .navigationBarsPadding()
                        .padding(20.dp),
            ) {
                content()
            }
        }
    }
}

/**
 * Returns the glass material for the sheet surface.
 *
 * On iOS 26 this uses the dedicated `UIGlassEffect` (the same typed binding used by
 * `LiquidSurface`); on earlier systems it falls back to a system [UIBlurEffect]
 * (`systemMaterial`), which renders as the closest available material.
 */
private fun liquidGlassEffect(): UIVisualEffect {
    if (isiOS26OrLater()) {
        return UIGlassEffect.effectWithStyle(UIGlassEffectStyle.UIGlassEffectStyleRegular)
    }
    return UIBlurEffect.effectWithStyle(UIBlurEffectStyle.UIBlurEffectStyleSystemMaterial)
}

private fun isiOS26OrLater(): Boolean =
    NSProcessInfo.processInfo.operatingSystemVersion.useContents { majorVersion >= 26L }
