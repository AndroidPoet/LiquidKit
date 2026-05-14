package io.github.androidpoet.liquidkit.toggle

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import io.github.androidpoet.liquidkit.LiquidGlassStyle
import io.github.androidpoet.liquidkit.surface.LiquidSurface

@Composable
public fun LiquidToggle(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    style: LiquidGlassStyle = LiquidGlassStyle.Control,
) {
    LiquidSurface(
        modifier = modifier
            .width(58.dp)
            .height(34.dp)
            .toggleable(
                value = checked,
                enabled = enabled,
                role = Role.Switch,
                onValueChange = onCheckedChange,
            ),
        style = if (checked) {
            style.copy(containerColor = style.selectedContainerColor)
        } else {
            style
        },
    ) {
        Box(
            modifier = Modifier
                .padding(4.dp)
                .size(26.dp)
                .align(if (checked) Alignment.CenterEnd else Alignment.CenterStart)
                .offset(x = if (checked) (-2).dp else 2.dp)
                .clip(CircleShape)
                .background(if (checked) style.selectedContentColor else Color.White.copy(alpha = 0.82f)),
        )
    }
}
