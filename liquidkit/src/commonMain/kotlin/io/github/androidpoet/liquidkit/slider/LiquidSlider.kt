package io.github.androidpoet.liquidkit.slider

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.semantics.ProgressBarRangeInfo
import androidx.compose.ui.semantics.progressBarRangeInfo
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import io.github.androidpoet.liquidkit.LiquidGlassStyle
import io.github.androidpoet.liquidkit.surface.LiquidSurface
import kotlin.math.roundToInt

@Composable
public fun LiquidSlider(
    value: Float,
    onValueChange: (Float) -> Unit,
    modifier: Modifier = Modifier,
    valueRange: ClosedFloatingPointRange<Float> = 0f..1f,
    enabled: Boolean = true,
    style: LiquidGlassStyle = LiquidGlassStyle.Control,
) {
    require(valueRange.endInclusive > valueRange.start) {
        "LiquidSlider requires a valueRange with endInclusive greater than start."
    }

    var widthPx by remember { mutableIntStateOf(1) }
    val coercedValue = value.coerceIn(valueRange.start, valueRange.endInclusive)
    val fraction = ((coercedValue - valueRange.start) / (valueRange.endInclusive - valueRange.start))
        .coerceIn(0f, 1f)

    val dragState = rememberDraggableState { delta ->
        if (enabled) {
            onValueChange(valueFromFraction(fraction + delta / widthPx, valueRange))
        }
    }

    BoxWithConstraints(
        modifier = modifier
            .height(44.dp)
            .onSizeChanged { widthPx = it.width.coerceAtLeast(1) }
            .semantics {
                progressBarRangeInfo = ProgressBarRangeInfo(
                    current = coercedValue,
                    range = valueRange,
                    steps = 0,
                )
            }
            .pointerInput(enabled, valueRange) {
                detectTapGestures { offset ->
                    if (enabled) {
                        onValueChange(valueFromFraction(offset.x / size.width, valueRange))
                    }
                }
            }
            .draggable(
                enabled = enabled,
                orientation = Orientation.Horizontal,
                state = dragState,
            ),
        contentAlignment = Alignment.CenterStart,
    ) {
        LiquidSurface(
            modifier = Modifier
                .fillMaxWidth()
                .height(18.dp),
            style = style,
        ) {
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth(fraction)
                    .background(style.selectedContainerColor),
            )
        }

        Box(
            modifier = Modifier
                .offset {
                    IntOffset(
                        x = (widthPx * fraction).roundToInt() - 13.dp.roundToPx(),
                        y = 0,
                    )
                }
                .size(26.dp)
                .clip(CircleShape)
                .background(style.selectedContentColor),
        )
    }
}

private fun valueFromFraction(
    fraction: Float,
    valueRange: ClosedFloatingPointRange<Float>,
): Float {
    val coercedFraction = fraction.coerceIn(0f, 1f)
    return valueRange.start + (valueRange.endInclusive - valueRange.start) * coercedFraction
}
