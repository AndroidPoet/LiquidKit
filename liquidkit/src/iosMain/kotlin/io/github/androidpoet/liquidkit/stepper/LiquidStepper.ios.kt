package io.github.androidpoet.liquidkit.stepper

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
import platform.UIKit.UIStepper
import platform.darwin.NSObject

@OptIn(BetaInteropApi::class)
private class LiquidStepperTarget(
    var onValueChange: (Float) -> Unit,
) : NSObject() {
    @ObjCAction
    fun valueChanged(sender: UIStepper) {
        onValueChange(sender.value.toFloat())
    }
}

@OptIn(BetaInteropApi::class, ExperimentalForeignApi::class, ExperimentalComposeUiApi::class)
@Composable
internal actual fun PlatformLiquidStepper(
    value: Float,
    onValueChange: (Float) -> Unit,
    modifier: Modifier,
    valueRange: ClosedFloatingPointRange<Float>,
    step: Float,
    enabled: Boolean,
    style: LiquidGlassStyle,
) {
    val currentOnValueChange = rememberUpdatedState(onValueChange)
    val target = remember {
        LiquidStepperTarget { currentOnValueChange.value(it) }
    }
    target.onValueChange = { currentOnValueChange.value(it) }

    UIKitView(
        factory = {
            UIStepper().apply {
                minimumValue = valueRange.start.toDouble()
                maximumValue = valueRange.endInclusive.toDouble()
                stepValue = step.toDouble()
                this.value = value.toDouble()
                this.enabled = enabled
                tintColor = style.selectedContentColor.toUIColor()
                backgroundColor = UIColor.clearColor
                addTarget(
                    target = target,
                    action = NSSelectorFromString("valueChanged:"),
                    forControlEvents = UIControlEventValueChanged,
                )
            }
        },
        modifier = modifier.height(44.dp),
        update = { stepper ->
            stepper.minimumValue = valueRange.start.toDouble()
            stepper.maximumValue = valueRange.endInclusive.toDouble()
            stepper.stepValue = step.toDouble()
            if (stepper.value != value.toDouble()) {
                stepper.value = value.toDouble()
            }
            stepper.enabled = enabled
            stepper.tintColor = style.selectedContentColor.toUIColor()
        },
        onRelease = {},
        properties = UIKitInteropProperties(
            interactionMode = UIKitInteropInteractionMode.Cooperative(),
            isNativeAccessibilityEnabled = true,
            placedAsOverlay = true,
        ),
    )
}
