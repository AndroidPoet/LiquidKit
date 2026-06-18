package io.github.androidpoet.liquidkit.sample.showcase

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicText
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
import io.github.androidpoet.liquidkit.LiquidGlassStyle
import io.github.androidpoet.liquidkit.sheet.LiquidSheet
import io.github.androidpoet.liquidkit.toolbar.LiquidToolbar

/**
 * Self-contained showcase for [LiquidToolbar] and [LiquidSheet].
 *
 * Drops into any host without touching the shared sample nav: renders a glass
 * toolbar at the top with leading/trailing action slots, and a button that opens
 * a glass bottom sheet.
 */
@Composable
public fun ChromeShowcase(modifier: Modifier = Modifier) {
    var sheetVisible by remember { mutableStateOf(false) }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(Color(0xFF6D83F2), Color(0xFFA66BF0), Color(0xFFF26D9C)),
                ),
            ),
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .safeDrawingPadding()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp),
        ) {
            LiquidToolbar(
                title = {
                    BasicText(
                        text = "Liquid Chrome",
                        style = TextStyle(
                            color = Color(0xFF101418),
                            fontSize = 17.sp,
                            fontWeight = FontWeight.SemiBold,
                        ),
                    )
                },
                leading = {
                    BasicText(
                        text = "Back",
                        modifier = Modifier.clickable { /* no-op */ },
                        style = TextStyle(color = Color(0xFF0A84FF), fontSize = 15.sp),
                    )
                },
                trailing = {
                    BasicText(
                        text = "Done",
                        modifier = Modifier.clickable { /* no-op */ },
                        style = TextStyle(
                            color = Color(0xFF0A84FF),
                            fontSize = 15.sp,
                            fontWeight = FontWeight.SemiBold,
                        ),
                    )
                },
            )

            Spacer(Modifier.height(8.dp))

            // "Open sheet" pill.
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White.copy(alpha = 0.38f), RoundedCornerShape(22.dp))
                    .clickable { sheetVisible = true }
                    .padding(vertical = 14.dp),
                contentAlignment = Alignment.Center,
            ) {
                BasicText(
                    text = "Open glass sheet",
                    style = TextStyle(
                        color = Color(0xFF101418),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                    ),
                )
            }
        }

        LiquidSheet(
            visible = sheetVisible,
            onDismiss = { sheetVisible = false },
            style = LiquidGlassStyle.Surface,
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                BasicText(
                    text = "Liquid Glass Sheet",
                    style = TextStyle(
                        color = Color(0xFF101418),
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                    ),
                )
                BasicText(
                    text = "A glass-backed bottom sheet. Tap the scrim to dismiss.",
                    style = TextStyle(color = Color(0xFF4E5968), fontSize = 14.sp),
                )
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFF0A84FF), RoundedCornerShape(20.dp))
                        .clickable { sheetVisible = false }
                        .padding(vertical = 12.dp),
                    contentAlignment = Alignment.Center,
                ) {
                    BasicText(
                        text = "Dismiss",
                        style = TextStyle(
                            color = Color.White,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold,
                        ),
                    )
                }
            }
        }
    }
}
