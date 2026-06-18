package io.github.androidpoet.liquidkit.toolbar

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.UIKitInteropInteractionMode
import androidx.compose.ui.viewinterop.UIKitInteropProperties
import androidx.compose.ui.viewinterop.UIKitView
import io.github.androidpoet.liquidkit.LiquidGlassStyle
import kotlinx.cinterop.ExperimentalForeignApi
import platform.CoreGraphics.CGRectMake
import platform.UIKit.UIColor
import platform.UIKit.UINavigationBar
import platform.UIKit.UINavigationBarAppearance
import platform.UIKit.UINavigationItem

/**
 * iOS [LiquidToolbar] backed by a genuine native [UINavigationBar].
 *
 * The native bar provides the glass surface (auto Liquid Glass on iOS 26, system
 * translucent bar below) while the Compose [title]/[leading]/[trailing] slots are
 * overlaid on top so callers keep an arbitrary Compose slot API.
 */
@OptIn(ExperimentalForeignApi::class, ExperimentalComposeUiApi::class)
@Composable
internal actual fun PlatformLiquidToolbar(
    title: @Composable () -> Unit,
    modifier: Modifier,
    leading: (@Composable () -> Unit)?,
    trailing: (@Composable () -> Unit)?,
    style: LiquidGlassStyle,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp),
    ) {
        // Native glass background.
        UIKitView(
            factory = {
                UINavigationBar(frame = CGRectMake(0.0, 0.0, 400.0, 56.0)).apply {
                    configureLiquidNavigationBar()
                }
            },
            modifier = Modifier.fillMaxSize(),
            update = { bar -> bar.configureLiquidNavigationBar() },
            onRelease = {},
            properties = UIKitInteropProperties(
                interactionMode = null,
                isNativeAccessibilityEnabled = true,
                placedAsOverlay = false,
            ),
        )

        // Compose slot overlay.
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 12.dp),
            contentAlignment = Alignment.Center,
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Box(modifier = Modifier.widthIn(min = 40.dp)) {
                    leading?.invoke()
                }
                Box(
                    modifier = Modifier.widthIn(min = 40.dp),
                    contentAlignment = Alignment.CenterEnd,
                ) {
                    trailing?.invoke()
                }
            }
            Box(contentAlignment = Alignment.Center) {
                title()
            }
        }
    }
}

private fun UINavigationBar.configureLiquidNavigationBar() {
    // An empty nav item lets the bar render its (glass) chrome without native text,
    // since the title is supplied by the Compose overlay.
    setItems(listOf(UINavigationItem(title = "")), animated = false)
    val appearance = UINavigationBarAppearance().apply {
        // Default background opts the bar into the system material — Liquid Glass on iOS 26.
        configureWithDefaultBackground()
    }
    standardAppearance = appearance
    scrollEdgeAppearance = appearance
    compactAppearance = appearance
    translucent = true
    backgroundColor = UIColor.clearColor
}
