package io.github.androidpoet.liquidkit.play

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
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
import platform.UIKit.UIButtonConfiguration
import platform.UIKit.UIColor
import platform.UIKit.UIControlEventValueChanged
import platform.UIKit.UIControlStateNormal
import platform.UIKit.UIImage
import platform.UIKit.UISlider
import platform.darwin.NSObject

@OptIn(BetaInteropApi::class)
private class LiquidScrubberTarget(
    var onValueChange: (Float) -> Unit,
) : NSObject() {
    @ObjCAction
    fun valueChanged(sender: UISlider) {
        onValueChange(sender.value)
    }
}

@OptIn(BetaInteropApi::class)
private class LiquidPlayPauseTarget(
    var onToggle: () -> Unit,
) : NSObject() {
    @ObjCAction
    fun tapped() {
        onToggle()
    }
}

@OptIn(BetaInteropApi::class, ExperimentalForeignApi::class, ExperimentalComposeUiApi::class)
@Composable
internal actual fun PlatformLiquidMediaControl(
    isPlaying: Boolean,
    onPlayPauseToggle: (Boolean) -> Unit,
    progress: Float,
    onProgressChange: (Float) -> Unit,
    modifier: Modifier,
    progressRange: ClosedFloatingPointRange<Float>,
    enabled: Boolean,
    style: LiquidGlassStyle,
) {
    val currentOnProgressChange = rememberUpdatedState(onProgressChange)
    val currentOnToggle = rememberUpdatedState(onPlayPauseToggle)
    val currentIsPlaying = rememberUpdatedState(isPlaying)

    val scrubberTarget = remember {
        LiquidScrubberTarget { currentOnProgressChange.value(it) }
    }
    scrubberTarget.onValueChange = { currentOnProgressChange.value(it) }

    val playPauseTarget = remember {
        LiquidPlayPauseTarget { currentOnToggle.value(!currentIsPlaying.value) }
    }
    playPauseTarget.onToggle = { currentOnToggle.value(!currentIsPlaying.value) }

    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        // Genuine native glass play/pause button (SF Symbol, iOS 26 glass configuration).
        UIKitView(
            factory = {
                val config = UIButtonConfiguration.glassButtonConfiguration()
                UIButton().apply {
                    configuration = config
                    applyPlayPauseImage(currentIsPlaying.value)
                    this.enabled = enabled
                    tintColor = style.selectedContentColor.toUIColor()
                    backgroundColor = UIColor.clearColor
                    addTarget(
                        target = playPauseTarget,
                        action = NSSelectorFromString("tapped"),
                        forControlEvents = platform.UIKit.UIControlEventTouchUpInside,
                    )
                }
            },
            modifier = Modifier.requiredSize(width = 56.dp, height = 44.dp),
            update = { button ->
                button.applyPlayPauseImage(currentIsPlaying.value)
                button.enabled = enabled
                button.tintColor = style.selectedContentColor.toUIColor()
            },
            onRelease = {},
            properties = UIKitInteropProperties(
                interactionMode = UIKitInteropInteractionMode.Cooperative(),
                isNativeAccessibilityEnabled = true,
                placedAsOverlay = true,
            ),
        )

        // Genuine native scrubber.
        UIKitView(
            factory = {
                UISlider().apply {
                    minimumValue = progressRange.start
                    maximumValue = progressRange.endInclusive
                    setValue(progress, animated = false)
                    this.enabled = enabled
                    minimumTrackTintColor = style.selectedContentColor.copy(alpha = 0.58f).toUIColor()
                    maximumTrackTintColor = style.contentColor.copy(alpha = 0.16f).toUIColor()
                    thumbTintColor = UIColor.whiteColor
                    backgroundColor = UIColor.clearColor
                    addTarget(
                        target = scrubberTarget,
                        action = NSSelectorFromString("valueChanged:"),
                        forControlEvents = UIControlEventValueChanged,
                    )
                }
            },
            modifier = Modifier.weight(1f).height(44.dp),
            update = { slider ->
                slider.minimumValue = progressRange.start
                slider.maximumValue = progressRange.endInclusive
                if (slider.value != progress) {
                    slider.setValue(progress, animated = true)
                }
                slider.enabled = enabled
                slider.minimumTrackTintColor =
                    style.selectedContentColor.copy(alpha = if (enabled) 0.58f else 0.26f).toUIColor()
                slider.maximumTrackTintColor =
                    style.contentColor.copy(alpha = if (enabled) 0.16f else 0.10f).toUIColor()
                slider.thumbTintColor = UIColor.whiteColor
            },
            onRelease = {},
            properties = UIKitInteropProperties(
                interactionMode = UIKitInteropInteractionMode.Cooperative(),
                isNativeAccessibilityEnabled = true,
                placedAsOverlay = true,
            ),
        )
    }
}

private fun UIButton.applyPlayPauseImage(isPlaying: Boolean) {
    val symbol = if (isPlaying) "pause.fill" else "play.fill"
    setImage(UIImage.systemImageNamed(symbol), forState = UIControlStateNormal)
}
