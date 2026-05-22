package io.github.androidpoet.liquidkit.play

import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.height
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
import io.github.androidpoet.liquidkit.internal.toUIColor
import kotlinx.cinterop.BetaInteropApi
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.ObjCAction
import platform.Foundation.NSSelectorFromString
import platform.UIKit.UIButton
import platform.UIKit.UIButtonTypeSystem
import platform.UIKit.UIColor
import platform.UIKit.UIControlEventTouchUpInside
import platform.darwin.NSObject

@OptIn(BetaInteropApi::class)
private class LiquidPlayButtonTarget(
    var onTap: () -> Unit,
) : NSObject() {
    @ObjCAction
    fun tapped() {
        onTap()
    }
}

@OptIn(BetaInteropApi::class, ExperimentalForeignApi::class, ExperimentalComposeUiApi::class)
@Composable
internal actual fun PlatformLiquidPlayButton(
    playing: Boolean,
    onPlayingChange: (Boolean) -> Unit,
    modifier: Modifier,
    enabled: Boolean,
    style: LiquidGlassStyle,
) {
    val currentOnPlayingChange = rememberUpdatedState(onPlayingChange)
    val target = remember {
        LiquidPlayButtonTarget { currentOnPlayingChange.value(!playing) }
    }
    target.onTap = { currentOnPlayingChange.value(!playing) }

    UIKitView(
        factory = {
            UIButton.buttonWithType(UIButtonTypeSystem).apply {
                configureLiquidPlayButton(playing, enabled, style)
                addTarget(
                    target = target,
                    action = NSSelectorFromString("tapped"),
                    forControlEvents = UIControlEventTouchUpInside,
                )
            }
        },
        modifier = modifier
            .defaultMinSize(minWidth = 96.dp)
            .height(48.dp),
        update = { button ->
            button.configureLiquidPlayButton(playing, enabled, style)
        },
        onRelease = {},
        properties = UIKitInteropProperties(
            interactionMode = UIKitInteropInteractionMode.Cooperative(),
            isNativeAccessibilityEnabled = true,
            placedAsOverlay = true,
        ),
    )
}

private fun UIButton.configureLiquidPlayButton(
    playing: Boolean,
    enabled: Boolean,
    style: LiquidGlassStyle,
) {
    setTitle(if (playing) "Pause" else "Play", forState = 0u)
    this.enabled = enabled
    backgroundColor = style.selectedContainerColor.toUIColor()
    tintColor = style.selectedContentColor.toUIColor()
    setTitleColor(style.selectedContentColor.toUIColor(), forState = 0u)
    layer.cornerRadius = 24.0
    layer.masksToBounds = true
    setOpaque(false)
    if (!enabled) {
        alpha = 0.42
    } else {
        alpha = 1.0
    }
}
