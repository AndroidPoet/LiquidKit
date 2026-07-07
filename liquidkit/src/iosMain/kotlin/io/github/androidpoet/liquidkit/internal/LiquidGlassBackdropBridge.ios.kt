package io.github.androidpoet.liquidkit.internal

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
public actual fun rememberLiquidLayerBackdrop(): Any? = null

public actual fun Modifier.captureToLiquidLayerBackdrop(backdrop: Any?): Modifier = this
