package io.github.androidpoet.liquidkit.surface

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.viewinterop.UIKitView
import io.github.androidpoet.liquidkit.LiquidGlassStyle
import kotlinx.cinterop.ExperimentalForeignApi
import platform.UIKit.UIBlurEffect
import platform.UIKit.UIBlurEffectStyle
import platform.UIKit.UIVisualEffectView

@OptIn(ExperimentalForeignApi::class)
@Composable
internal actual fun PlatformLiquidSurface(
    modifier: Modifier,
    style: LiquidGlassStyle,
    content: @Composable BoxScope.() -> Unit,
) {
    Box(
        modifier = modifier.clip(RoundedCornerShape(style.cornerRadius)),
    ) {
        UIKitView(
            factory = {
                UIVisualEffectView(
                    effect = UIBlurEffect.effectWithStyle(UIBlurEffectStyle.UIBlurEffectStyleRegular),
                )
            },
            modifier = Modifier.fillMaxSize(),
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(style.containerColor),
            content = content,
        )
    }
}
