package io.github.androidpoet.liquidkit.slider

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
import platform.UIKit.UIColor
import platform.UIKit.UIControlEventValueChanged
import platform.UIKit.UISlider
import platform.darwin.NSObject

@OptIn(BetaInteropApi::class)
private class LiquidSliderTarget(
    var onValueChange: (Float) -> Unit,
) : NSObject() {
    @ObjCAction
    fun valueChanged(sender: UISlider) {
        onValueChange(sender.value)
    }
}

@OptIn(BetaInteropApi::class, ExperimentalForeignApi::class, ExperimentalComposeUiApi::class)
@Composable
internal actual fun PlatformLiquidSlider(
    value: Float,
    onValueChange: (Float) -> Unit,
    modifier: Modifier,
    valueRange: ClosedFloatingPointRange<Float>,
    enabled: Boolean,
    style: LiquidGlassStyle,
) {
    val currentOnValueChange = rememberUpdatedState(onValueChange)
    val target = remember {
        LiquidSliderTarget { currentOnValueChange.value(it) }
    }
    target.onValueChange = { currentOnValueChange.value(it) }

    UIKitView(
        factory = {
            UISlider().apply {
                minimumValue = valueRange.start
                maximumValue = valueRange.endInclusive
                setValue(value, animated = false)
                this.enabled = enabled
                minimumTrackTintColor = style.selectedContentColor.copy(alpha = 0.58f).toUIColor()
                maximumTrackTintColor = style.contentColor.copy(alpha = 0.16f).toUIColor()
                thumbTintColor = UIColor.whiteColor
                backgroundColor = UIColor.clearColor
                addTarget(
                    target = target,
                    action = NSSelectorFromString("valueChanged:"),
                    forControlEvents = UIControlEventValueChanged,
                )
            }
        },
        modifier = modifier.height(44.dp),
        update = { slider ->
            slider.minimumValue = valueRange.start
            slider.maximumValue = valueRange.endInclusive
            if (slider.value != value) {
                slider.setValue(value, animated = true)
            }
            slider.enabled = enabled
            slider.minimumTrackTintColor = style.selectedContentColor.copy(alpha = if (enabled) 0.58f else 0.26f).toUIColor()
            slider.maximumTrackTintColor = style.contentColor.copy(alpha = if (enabled) 0.16f else 0.10f).toUIColor()
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
