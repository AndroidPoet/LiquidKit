package io.github.androidpoet.liquidkit.sample.showcase

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.github.androidpoet.liquidkit.LiquidGlassStyle
import io.github.androidpoet.liquidkit.LiquidKitTheme
import io.github.androidpoet.liquidkit.play.LiquidMediaControl
import io.github.androidpoet.liquidkit.stepper.LiquidStepper

/**
 * Self-contained showcase for [LiquidStepper] and [LiquidMediaControl].
 *
 * Renders both new parity components over a colorful gradient so the Liquid Glass surfaces are
 * visible. Drop this composable anywhere to preview the components in isolation.
 */
@Composable
public fun StepperMediaShowcase() {
    LiquidKitTheme(style = LiquidGlassStyle.SurfaceDark) {
        Column(
            modifier =
                Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            listOf(
                                Color(0xFF1E2A78),
                                Color(0xFF6A1B9A),
                                Color(0xFFFF6F00),
                            ),
                        ),
                    ).safeDrawingPadding()
                    .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(32.dp),
        ) {
            SectionLabel("Liquid Stepper")

            var quantity by remember { mutableFloatStateOf(2f) }
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                ValueLabel("Quantity: ${quantity.toInt()}")
                LiquidStepper(
                    value = quantity,
                    onValueChange = { quantity = it },
                    valueRange = 0f..10f,
                    step = 1f,
                )
            }

            Spacer(Modifier.height(8.dp))

            SectionLabel("Liquid Media Control")

            var isPlaying by remember { mutableStateOf(false) }
            var progress by remember { mutableFloatStateOf(0.35f) }
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                ValueLabel(
                    (if (isPlaying) "Playing" else "Paused") +
                        "  •  ${(progress * 100).toInt()}%",
                )
                LiquidMediaControl(
                    isPlaying = isPlaying,
                    onPlayPauseToggle = { isPlaying = it },
                    progress = progress,
                    onProgressChange = { progress = it },
                    modifier = Modifier.fillMaxWidth(),
                )
            }
        }
    }
}

@Composable
private fun SectionLabel(text: String) {
    BasicText(
        text = text,
        style =
            TextStyle(
                color = Color.White,
                fontSize = 20.sp,
                fontWeight = FontWeight.SemiBold,
            ),
    )
}

@Composable
private fun ValueLabel(text: String) {
    BasicText(
        text = text,
        style =
            TextStyle(
                color = Color.White.copy(alpha = 0.85f),
                fontSize = 15.sp,
            ),
    )
}
