package io.github.androidpoet.liquidkit.internal

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import io.github.androidpoet.liquidkit.internal.androidglass.backdrop.backdrops.LayerBackdrop
import io.github.androidpoet.liquidkit.internal.androidglass.backdrop.backdrops.layerBackdrop
import io.github.androidpoet.liquidkit.internal.androidglass.backdrop.backdrops.rememberLayerBackdrop

@Composable
public actual fun rememberLiquidLayerBackdrop(): Any? = rememberLayerBackdrop()

public actual fun Modifier.captureToLiquidLayerBackdrop(backdrop: Any?): Modifier =
    if (backdrop is LayerBackdrop) layerBackdrop(backdrop) else this
