package io.github.androidpoet.liquidkit.internal.androidglass.backdrop.effects

import android.graphics.RenderEffect
import android.os.Build
import io.github.androidpoet.liquidkit.internal.androidglass.backdrop.BackdropEffectScope

fun BackdropEffectScope.effect(effect: RenderEffect) {
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.S) return

    val currentEffect = renderEffect
    renderEffect =
        if (currentEffect != null) {
            RenderEffect.createChainEffect(effect, currentEffect)
        } else {
            effect
        }
}

fun BackdropEffectScope.effect(effect: androidx.compose.ui.graphics.RenderEffect) {
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.S) return

    effect(effect.asAndroidRenderEffect())
}
