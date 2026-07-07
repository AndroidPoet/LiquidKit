@file:OptIn(io.github.androidpoet.liquidkit.internal.InternalLiquidKitApi::class)

package io.github.androidpoet.liquidkit.dropdown

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import com.kyant.shapes.Capsule
import io.github.androidpoet.liquidkit.LiquidGlassStyle
import io.github.androidpoet.liquidkit.internal.LocalLiquidLayerBackdrop
import io.github.androidpoet.liquidkit.internal.androidglass.backdrop.Backdrop
import io.github.androidpoet.liquidkit.internal.androidglass.backdrop.backdrops.LayerBackdrop
import io.github.androidpoet.liquidkit.internal.androidglass.backdrop.backdrops.rememberCanvasBackdrop
import io.github.androidpoet.liquidkit.internal.androidglass.backdrop.drawBackdrop
import io.github.androidpoet.liquidkit.internal.androidglass.backdrop.effects.blur
import io.github.androidpoet.liquidkit.internal.androidglass.backdrop.effects.lens
import io.github.androidpoet.liquidkit.internal.androidglass.backdrop.effects.vibrancy
import io.github.androidpoet.liquidkit.internal.androidglass.backdrop.highlight.Highlight
import io.github.androidpoet.liquidkit.internal.androidglass.backdrop.shadow.InnerShadow
import io.github.androidpoet.liquidkit.internal.androidglass.backdrop.shadow.Shadow

@Composable
internal actual fun <T : Any> PlatformLiquidDropdownMenu(
    items: List<LiquidMenuItem<T>>,
    onSelect: (T) -> Unit,
    modifier: Modifier,
    label: String,
    selectedKey: T?,
    enabled: Boolean,
    style: LiquidGlassStyle,
) {
    val isLight = !isSystemInDarkTheme()
    var expanded by remember { mutableStateOf(false) }

    val layerCapture = LocalLiquidLayerBackdrop.current
    val canvasBackdrop = rememberCanvasBackdrop { drawRect(style.containerColor) }
    val backdrop: Backdrop = if (layerCapture is LayerBackdrop) layerCapture else canvasBackdrop

    val anchorSurface = if (isLight) Color.Black.copy(0.05f) else Color.White.copy(0.08f)

    Box(modifier.alpha(if (enabled) 1f else 0.42f)) {
        // Anchor: glass capsule button
        Row(
            Modifier
                .drawBackdrop(
                    backdrop = backdrop,
                    shape = { Capsule() },
                    effects = {
                        vibrancy()
                        blur(2f.dp.toPx())
                        lens(12f.dp.toPx(), 24f.dp.toPx())
                    },
                    highlight = { Highlight.Default },
                    shadow = { Shadow(alpha = 0.12f) },
                    onDrawSurface = { drawRect(anchorSurface) },
                ).clickable(
                    interactionSource = null,
                    indication = null,
                    enabled = enabled,
                    role = Role.DropdownList,
                    onClick = { expanded = true },
                ).height(44.dp)
                .padding(horizontal = 18.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp),
        ) {
            BasicText(
                text = label,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style =
                    TextStyle(
                        color = style.selectedContentColor,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.SemiBold,
                    ),
            )
            BasicText(
                text = "▾",
                style = TextStyle(color = style.contentColor, fontSize = 12.sp),
            )
        }

        if (expanded) {
            val transitionState = remember { MutableTransitionState(false) }
            transitionState.targetState = true

            Popup(
                alignment = Alignment.TopStart,
                offset = IntOffset(0, with(androidx.compose.ui.platform.LocalDensity.current) { 50.dp.roundToPx() }),
                onDismissRequest = { expanded = false },
                properties = PopupProperties(focusable = true),
            ) {
                AnimatedVisibility(
                    visibleState = transitionState,
                    enter = fadeIn(spring()) + scaleIn(spring(), initialScale = 0.9f, transformOrigin = TransformOrigin(0f, 0f)),
                    exit = fadeOut() + scaleOut(targetScale = 0.9f),
                ) {
                    DropdownGlassSurface(
                        items = items,
                        selectedKey = selectedKey,
                        backdrop = backdrop,
                        style = style,
                        isLight = isLight,
                        onSelect = {
                            expanded = false
                            onSelect(it)
                        },
                    )
                }
            }
        }
    }
}

@Composable
private fun <T : Any> DropdownGlassSurface(
    items: List<LiquidMenuItem<T>>,
    selectedKey: T?,
    backdrop: Backdrop,
    style: LiquidGlassStyle,
    isLight: Boolean,
    onSelect: (T) -> Unit,
) {
    val menuSurface = if (isLight) Color.White.copy(0.55f) else Color.Black.copy(0.45f)
    androidx.compose.foundation.layout.Column(
        Modifier
            .widthIn(min = 200.dp)
            .drawBackdrop(
                backdrop = backdrop,
                shape = { RoundedCornerShape(20.dp) },
                effects = {
                    vibrancy()
                    blur(12f.dp.toPx())
                    lens(10f.dp.toPx(), 20f.dp.toPx())
                },
                highlight = { Highlight.Default },
                shadow = { Shadow(alpha = 0.22f) },
                innerShadow = { InnerShadow(radius = 6f.dp, alpha = 0.10f) },
                onDrawSurface = { drawRect(menuSurface) },
            ).padding(6.dp),
    ) {
        items.forEach { item ->
            val isSelected = selectedKey != null && item.key == selectedKey
            val contentColor = if (isSelected) style.selectedContentColor else style.contentColor
            Row(
                Modifier
                    .fillMaxWidth()
                    .clickable(
                        interactionSource = null,
                        indication = null,
                        enabled = item.enabled,
                        role = Role.Button,
                        onClick = { onSelect(item.key) },
                    ).alpha(if (item.enabled) 1f else 0.4f)
                    .padding(horizontal = 12.dp, vertical = 11.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp),
            ) {
                item.icon?.vectorFor(isSelected)?.let { vector ->
                    Image(
                        painter = rememberVectorPainter(vector),
                        contentDescription = item.icon.contentDescription,
                        modifier = Modifier.size(18.dp),
                        colorFilter = ColorFilter.tint(contentColor),
                    )
                }
                BasicText(
                    text = item.label,
                    modifier = Modifier.weight(1f),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    style =
                        TextStyle(
                            color = contentColor,
                            fontSize = 15.sp,
                            fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Medium,
                        ),
                )
                Spacer(Modifier.width(8.dp))
                if (isSelected) {
                    BasicText(
                        text = "✓",
                        style = TextStyle(color = style.selectedContentColor, fontSize = 14.sp, fontWeight = FontWeight.Bold),
                    )
                }
            }
        }
    }
}
