package io.github.androidpoet.liquidkit

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp

public fun liquidGlassStyle(
    base: LiquidGlassStyle = LiquidGlassStyle.Control,
    cornerRadius: Dp = base.cornerRadius,
    containerColor: Color = base.containerColor,
    selectedContainerColor: Color = base.selectedContainerColor,
    contentColor: Color = base.contentColor,
    selectedContentColor: Color = base.selectedContentColor,
    blurRadius: Dp = base.blurRadius,
    refractionHeight: Dp = base.refractionHeight,
): LiquidGlassStyle = LiquidGlassStyle(
    cornerRadius = cornerRadius,
    containerColor = containerColor,
    selectedContainerColor = selectedContainerColor,
    contentColor = contentColor,
    selectedContentColor = selectedContentColor,
    blurRadius = blurRadius,
    refractionHeight = refractionHeight,
)
