package io.github.androidpoet.liquidkit.sample.showcase

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.github.androidpoet.liquidkit.surface.LiquidGlassContainer
import io.github.androidpoet.liquidkit.surface.LiquidSurface

/**
 * Self-contained demo of [LiquidSurface] / [LiquidGlassContainer] over a colorful backdrop, so the
 * native glass refraction (Android engine / iOS UIGlassEffect) is clearly visible.
 */
@Composable
public fun SurfaceShowcase(modifier: Modifier = Modifier) {
    var interactive by remember { mutableStateOf(true) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(showcaseBackground())
            .safeDrawingPadding()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp, vertical = 28.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        BasicText(
            text = "Liquid Surface",
            modifier = Modifier.fillMaxWidth(),
            style = TextStyle(
                color = Color.White,
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold,
            ),
        )

        // A simple glass card with content on top.
        LiquidSurface(
            modifier = Modifier
                .fillMaxWidth()
                .height(140.dp),
        ) {
            CardLabel(
                title = "Default card",
                subtitle = "Native glass behind arbitrary content",
            )
        }

        // Tinted, interactive glass card.
        LiquidSurface(
            modifier = Modifier
                .fillMaxWidth()
                .height(140.dp),
            shape = RoundedCornerShape(36.dp),
            tint = Color(0xFF4FC3F7),
            interactive = interactive,
        ) {
            CardLabel(
                title = "Tinted + interactive",
                subtitle = "Press / drag on Android for the highlight",
            )
        }

        // A container grouping two glass children that share one sampling region (Android).
        LiquidGlassContainer(
            modifier = Modifier
                .fillMaxWidth()
                .height(160.dp),
        ) {
            LiquidSurface(
                modifier = Modifier
                    .fillMaxWidth(0.5f)
                    .height(120.dp)
                    .align(Alignment.CenterStart),
                tint = Color(0xFFFFB74D),
            ) {
                CardLabel(title = "Grouped A", subtitle = "Shared region")
            }
            LiquidSurface(
                modifier = Modifier
                    .fillMaxWidth(0.5f)
                    .height(120.dp)
                    .align(Alignment.CenterEnd),
                tint = Color(0xFFBA68C8),
            ) {
                CardLabel(title = "Grouped B", subtitle = "Shared region")
            }
        }
    }
}

@Composable
private fun CardLabel(
    title: String,
    subtitle: String,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(18.dp),
        verticalArrangement = Arrangement.Center,
    ) {
        BasicText(
            text = title,
            style = TextStyle(
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
            ),
        )
        BasicText(
            text = subtitle,
            style = TextStyle(
                color = Color.White.copy(alpha = 0.85f),
                fontSize = 13.sp,
            ),
        )
    }
}

private fun showcaseBackground(): Brush = Brush.linearGradient(
    colors = listOf(
        Color(0xFF1A237E),
        Color(0xFF6A1B9A),
        Color(0xFFAD1457),
        Color(0xFFEF6C00),
    ),
)
