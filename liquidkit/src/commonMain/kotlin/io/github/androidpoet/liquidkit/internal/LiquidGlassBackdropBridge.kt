package io.github.androidpoet.liquidkit.internal

import androidx.compose.runtime.Composable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Modifier

/**
 * Holds the platform capture layer used by glass components to sample from the
 * actual content drawn behind them.  On Android this is a [LayerBackdrop]; on iOS
 * it is null (native system handles blur).
 */
@InternalLiquidKitApi
public val LocalLiquidLayerBackdrop: androidx.compose.runtime.ProvidableCompositionLocal<Any?> =
    staticCompositionLocalOf { null }

@InternalLiquidKitApi
@Composable
public expect fun rememberLiquidLayerBackdrop(): Any?

@InternalLiquidKitApi
public expect fun Modifier.captureToLiquidLayerBackdrop(backdrop: Any?): Modifier
