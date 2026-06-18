@file:OptIn(io.github.androidpoet.liquidkit.internal.InternalLiquidKitApi::class)

package io.github.androidpoet.liquidkit.sheet

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import io.github.androidpoet.liquidkit.LiquidGlassStyle
import io.github.androidpoet.liquidkit.internal.LocalLiquidLayerBackdrop
import io.github.androidpoet.liquidkit.internal.androidglass.backdrop.Backdrop
import io.github.androidpoet.liquidkit.internal.androidglass.backdrop.backdrops.LayerBackdrop
import io.github.androidpoet.liquidkit.internal.androidglass.backdrop.backdrops.rememberCanvasBackdrop
import io.github.androidpoet.liquidkit.internal.androidglass.backdrop.drawBackdrop
import io.github.androidpoet.liquidkit.internal.androidglass.backdrop.effects.blur
import io.github.androidpoet.liquidkit.internal.androidglass.backdrop.effects.lens
import io.github.androidpoet.liquidkit.internal.androidglass.backdrop.effects.vibrancy

@Composable
internal actual fun PlatformLiquidSheet(
    visible: Boolean,
    onDismiss: () -> Unit,
    modifier: Modifier,
    style: LiquidGlassStyle,
    content: @Composable () -> Unit,
) {
    if (!visible) return

    Dialog(
        onDismissRequest = onDismiss,
        properties =
            DialogProperties(
                usePlatformDefaultWidth = false,
                dismissOnClickOutside = true,
            ),
    ) {
        val layerCapture = LocalLiquidLayerBackdrop.current
        val canvasBackdrop = rememberCanvasBackdrop { drawRect(style.containerColor) }
        val backdrop: Backdrop =
            if (layerCapture is LayerBackdrop) layerCapture else canvasBackdrop

        // Bottom-aligned scrim that fills the dialog window. A tap on the scrim
        // dismisses; the sheet surface itself sits at the bottom edge.
        Box(
            modifier =
                Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.32f)),
            contentAlignment = Alignment.BottomCenter,
        ) {
            val sheetShape =
                RoundedCornerShape(
                    topStart = style.cornerRadius,
                    topEnd = style.cornerRadius,
                    bottomStart = 0.dp,
                    bottomEnd = 0.dp,
                )
            Box(
                modifier =
                    modifier
                        .fillMaxWidth()
                        .widthIn(max = 640.dp)
                        .drawBackdrop(
                            backdrop = backdrop,
                            shape = { sheetShape },
                            effects = {
                                vibrancy()
                                blur(12f.dp.toPx())
                                lens(24f.dp.toPx(), 24f.dp.toPx())
                            },
                            onDrawSurface = { drawRect(style.containerColor) },
                        ).navigationBarsPadding()
                        .padding(20.dp),
            ) {
                content()
            }
        }
    }
}
