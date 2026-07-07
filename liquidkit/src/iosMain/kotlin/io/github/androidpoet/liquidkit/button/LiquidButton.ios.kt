package io.github.androidpoet.liquidkit.button

import androidx.compose.foundation.layout.heightIn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.UIKitInteropInteractionMode
import androidx.compose.ui.viewinterop.UIKitInteropProperties
import androidx.compose.ui.viewinterop.UIKitView
import io.github.androidpoet.liquidkit.LiquidGlassStyle
import io.github.androidpoet.liquidkit.internal.glassButtonConfigurationFor
import kotlinx.cinterop.BetaInteropApi
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.ObjCAction
import platform.Foundation.NSSelectorFromString
import platform.UIKit.UIButton
import platform.UIKit.UIColor
import platform.UIKit.UIControlEventTouchUpInside
import platform.UIKit.UIControlStateNormal
import platform.darwin.NSObject

@OptIn(BetaInteropApi::class)
private class LiquidButtonTarget(
    var onClick: () -> Unit,
) : NSObject() {
    @ObjCAction
    fun tapped() {
        onClick()
    }
}

@OptIn(BetaInteropApi::class, ExperimentalForeignApi::class, ExperimentalComposeUiApi::class)
@Composable
internal actual fun PlatformLiquidButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier,
    variant: LiquidButtonVariant,
    enabled: Boolean,
    style: LiquidGlassStyle,
) {
    val currentOnClick = rememberUpdatedState(onClick)
    val target = remember { LiquidButtonTarget { currentOnClick.value() } }
    target.onClick = { currentOnClick.value() }

    UIKitView(
        factory = {
            UIButton().apply {
                configuration =
                    glassButtonConfigurationFor(
                        prominent = variant == LiquidButtonVariant.GlassProminent,
                    )
                setTitle(text, forState = UIControlStateNormal)
                this.enabled = enabled
                backgroundColor = UIColor.clearColor
                setOpaque(false)
                addTarget(
                    target = target,
                    action = NSSelectorFromString("tapped"),
                    forControlEvents = UIControlEventTouchUpInside,
                )
            }
        },
        modifier = modifier.heightIn(min = 50.dp),
        update = { button ->
            button.setTitle(text, forState = UIControlStateNormal)
            button.enabled = enabled
        },
        onRelease = {},
        properties =
            UIKitInteropProperties(
                interactionMode = UIKitInteropInteractionMode.Cooperative(),
                isNativeAccessibilityEnabled = true,
                placedAsOverlay = true,
            ),
    )
}
