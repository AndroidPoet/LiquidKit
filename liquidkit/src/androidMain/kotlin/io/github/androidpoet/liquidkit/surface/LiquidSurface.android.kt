package io.github.androidpoet.liquidkit.surface

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import com.kyant.backdrop.backdrops.rememberCanvasBackdrop
import com.kyant.backdrop.drawBackdrop
import com.kyant.backdrop.effects.blur
import com.kyant.backdrop.effects.lens
import io.github.androidpoet.liquidkit.LiquidGlassStyle

@Composable
internal actual fun PlatformLiquidSurface(
    modifier: Modifier,
    style: LiquidGlassStyle,
    content: @Composable BoxScope.() -> Unit,
) {
    val backdrop = rememberCanvasBackdrop {
        drawRect(style.containerColor)
    }

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(style.cornerRadius))
            .drawBackdrop(
                backdrop = backdrop,
                shape = { RoundedCornerShape(style.cornerRadius) },
                effects = {
                    blur(style.blurRadius.toPx())
                    lens(style.refractionHeight.toPx(), style.refractionHeight.toPx())
                },
                onDrawSurface = { drawRect(style.containerColor) },
            ),
        content = content,
    )
}
