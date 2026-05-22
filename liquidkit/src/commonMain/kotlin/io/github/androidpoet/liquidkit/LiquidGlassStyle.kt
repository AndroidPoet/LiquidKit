package io.github.androidpoet.liquidkit

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Immutable
public data class LiquidGlassStyle(
    public val cornerRadius: Dp,
    public val containerColor: Color,
    public val selectedContainerColor: Color,
    public val contentColor: Color,
    public val selectedContentColor: Color,
) {
    public companion object {
        public val Surface: LiquidGlassStyle = LiquidGlassStyle(
            cornerRadius = 24.dp,
            containerColor = Color.White.copy(alpha = 0.28f),
            selectedContainerColor = Color.White.copy(alpha = 0.42f),
            contentColor = Color(0xFF4E5968),
            selectedContentColor = Color(0xFF101418),
        )

        public val Control: LiquidGlassStyle = LiquidGlassStyle(
            cornerRadius = 18.dp,
            containerColor = Color.White.copy(alpha = 0.34f),
            selectedContainerColor = Color(0xFF111820).copy(alpha = 0.08f),
            contentColor = Color(0xFF4E5968),
            selectedContentColor = Color(0xFF101418),
        )

        public val NavigationBar: LiquidGlassStyle = LiquidGlassStyle(
            cornerRadius = 28.dp,
            containerColor = Color.White.copy(alpha = 0.34f),
            selectedContainerColor = Color.White.copy(alpha = 0.42f),
            contentColor = Color(0xFF4E5968),
            selectedContentColor = Color(0xFF101418),
        )
    }
}
