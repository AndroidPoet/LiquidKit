package io.github.androidpoet.liquidkit.stepper

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import io.github.androidpoet.liquidkit.LiquidGlassStyle
import io.github.androidpoet.liquidkit.LocalLiquidGlassStyle

/**
 * A Liquid Glass increment/decrement stepper.
 *
 * On Android it renders with the LiquidKit glass backdrop engine; on iOS it uses a genuine native
 * `UIStepper` via UIKit interop so it picks up authentic system Liquid Glass on iOS 26.
 *
 * @param value the current value.
 * @param onValueChange invoked with the new value when the user increments or decrements.
 * @param modifier the [Modifier] to apply to this stepper.
 * @param valueRange the inclusive range the [value] is coerced into.
 * @param step the amount added or subtracted on each increment/decrement.
 * @param enabled whether the stepper responds to user input.
 * @param style the [LiquidGlassStyle] used to tint the glass surface.
 */
@Composable
public fun LiquidStepper(
    value: Float,
    onValueChange: (Float) -> Unit,
    modifier: Modifier = Modifier,
    valueRange: ClosedFloatingPointRange<Float> = 0f..10f,
    step: Float = 1f,
    enabled: Boolean = true,
    style: LiquidGlassStyle = LocalLiquidGlassStyle.current,
) {
    require(valueRange.start < valueRange.endInclusive) {
        "LiquidStepper requires a non-empty valueRange."
    }
    require(step > 0f) {
        "LiquidStepper requires a positive step."
    }

    PlatformLiquidStepper(
        value = value.coerceIn(valueRange),
        onValueChange = onValueChange,
        modifier = modifier,
        valueRange = valueRange,
        step = step,
        enabled = enabled,
        style = style,
    )
}

@Composable
internal expect fun PlatformLiquidStepper(
    value: Float,
    onValueChange: (Float) -> Unit,
    modifier: Modifier,
    valueRange: ClosedFloatingPointRange<Float>,
    step: Float,
    enabled: Boolean,
    style: LiquidGlassStyle,
)
