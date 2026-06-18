package io.github.androidpoet.liquidkit.sheet

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import io.github.androidpoet.liquidkit.LiquidGlassStyle

/**
 * A Liquid Glass bottom sheet.
 *
 * When [visible] is `true` a glass surface slides up from the bottom of the screen
 * hosting [content]. Tapping the scrim or requesting a dismiss invokes [onDismiss].
 *
 * On Android the sheet is a glass-backed Compose surface (a dialog/popup primitive
 * backed by the LiquidKit backdrop engine). On iOS it is a glass-backed surface using
 * a native `UIVisualEffectView` (iOS 26 `UIGlassEffect`, system blur fallback).
 *
 * NOTE: full native sheet *presentation* (`UISheetPresentationController` with detents,
 * drag-to-dismiss, etc.) is owned by the SwiftUI host. This in-Compose component renders
 * a native glass-backed surface inside the Compose tree, which is the correct scope for a
 * reusable cross-platform component.
 *
 * @param visible whether the sheet is shown.
 * @param onDismiss invoked when the user dismisses the sheet (scrim tap / back).
 * @param modifier applied to the sheet container.
 * @param style glass styling. Defaults to [LiquidGlassStyle.Surface].
 * @param content the sheet body.
 */
@Composable
public fun LiquidSheet(
    visible: Boolean,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
    style: LiquidGlassStyle = LiquidGlassStyle.Surface,
    content: @Composable () -> Unit,
) {
    PlatformLiquidSheet(
        visible = visible,
        onDismiss = onDismiss,
        modifier = modifier,
        style = style,
        content = content,
    )
}

@Composable
internal expect fun PlatformLiquidSheet(
    visible: Boolean,
    onDismiss: () -> Unit,
    modifier: Modifier,
    style: LiquidGlassStyle,
    content: @Composable () -> Unit,
)
