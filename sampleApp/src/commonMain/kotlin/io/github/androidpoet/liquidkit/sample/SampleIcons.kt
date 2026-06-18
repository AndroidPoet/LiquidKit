package io.github.androidpoet.liquidkit.sample

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

internal object SampleIcons {
    val Home: ImageVector by lazy {
        ImageVector
            .Builder(
                name = "Home",
                defaultWidth = 24.dp,
                defaultHeight = 24.dp,
                viewportWidth = 24f,
                viewportHeight = 24f,
            ).apply {
                path(
                    fill = null,
                    stroke = SolidColor(Color.Black),
                    strokeLineWidth = 1.8f,
                    strokeLineCap = StrokeCap.Round,
                    strokeLineJoin = StrokeJoin.Round,
                ) {
                    moveTo(4f, 11f)
                    lineTo(12f, 4f)
                    lineTo(20f, 11f)
                    lineTo(20f, 20f)
                    lineTo(15f, 20f)
                    lineTo(15f, 14f)
                    lineTo(9f, 14f)
                    lineTo(9f, 20f)
                    lineTo(4f, 20f)
                    close()
                }
            }.build()
    }

    val Controls: ImageVector by lazy {
        ImageVector
            .Builder(
                name = "Controls",
                defaultWidth = 24.dp,
                defaultHeight = 24.dp,
                viewportWidth = 24f,
                viewportHeight = 24f,
            ).apply {
                path(
                    fill = null,
                    stroke = SolidColor(Color.Black),
                    strokeLineWidth = 1.8f,
                    strokeLineCap = StrokeCap.Round,
                    strokeLineJoin = StrokeJoin.Round,
                ) {
                    moveTo(5f, 7f)
                    lineTo(19f, 7f)
                    moveTo(8f, 7f)
                    lineTo(8f, 7f)
                    moveTo(5f, 12f)
                    lineTo(19f, 12f)
                    moveTo(16f, 12f)
                    lineTo(16f, 12f)
                    moveTo(5f, 17f)
                    lineTo(19f, 17f)
                    moveTo(11f, 17f)
                    lineTo(11f, 17f)
                }
            }.build()
    }

    val Menu: ImageVector by lazy {
        ImageVector
            .Builder(
                name = "Menu",
                defaultWidth = 24.dp,
                defaultHeight = 24.dp,
                viewportWidth = 24f,
                viewportHeight = 24f,
            ).apply {
                path(
                    fill = null,
                    stroke = SolidColor(Color.Black),
                    strokeLineWidth = 1.9f,
                    strokeLineCap = StrokeCap.Round,
                    strokeLineJoin = StrokeJoin.Round,
                ) {
                    moveTo(5f, 7f)
                    lineTo(19f, 7f)
                    moveTo(5f, 12f)
                    lineTo(19f, 12f)
                    moveTo(5f, 17f)
                    lineTo(19f, 17f)
                }
            }.build()
    }

    val Sun: ImageVector by lazy {
        ImageVector
            .Builder(
                name = "Sun",
                defaultWidth = 24.dp,
                defaultHeight = 24.dp,
                viewportWidth = 24f,
                viewportHeight = 24f,
            ).apply {
                path(
                    fill = null,
                    stroke = SolidColor(Color.Black),
                    strokeLineWidth = 1.8f,
                    strokeLineCap = StrokeCap.Round,
                    strokeLineJoin = StrokeJoin.Round,
                ) {
                    moveTo(12f, 5f)
                    lineTo(12f, 3.5f)
                    moveTo(12f, 20.5f)
                    lineTo(12f, 19f)
                    moveTo(5f, 12f)
                    lineTo(3.5f, 12f)
                    moveTo(20.5f, 12f)
                    lineTo(19f, 12f)
                    moveTo(7.05f, 7.05f)
                    lineTo(6f, 6f)
                    moveTo(18f, 18f)
                    lineTo(16.95f, 16.95f)
                    moveTo(16.95f, 7.05f)
                    lineTo(18f, 6f)
                    moveTo(6f, 18f)
                    lineTo(7.05f, 16.95f)
                    moveTo(15.5f, 12f)
                    arcTo(3.5f, 3.5f, 0f, true, true, 8.5f, 12f)
                    arcTo(3.5f, 3.5f, 0f, true, true, 15.5f, 12f)
                }
            }.build()
    }

    val Moon: ImageVector by lazy {
        ImageVector
            .Builder(
                name = "Moon",
                defaultWidth = 24.dp,
                defaultHeight = 24.dp,
                viewportWidth = 24f,
                viewportHeight = 24f,
            ).apply {
                path(
                    fill = null,
                    stroke = SolidColor(Color.Black),
                    strokeLineWidth = 1.8f,
                    strokeLineCap = StrokeCap.Round,
                    strokeLineJoin = StrokeJoin.Round,
                ) {
                    moveTo(18.5f, 15.5f)
                    curveTo(16.9f, 16.7f, 14.8f, 17.2f, 12.8f, 16.7f)
                    curveTo(8.9f, 15.7f, 6.5f, 11.8f, 7.5f, 7.9f)
                    curveTo(7.8f, 6.8f, 8.3f, 5.8f, 9f, 5f)
                    curveTo(6.2f, 6f, 4.2f, 8.7f, 4.2f, 11.9f)
                    curveTo(4.2f, 16.2f, 7.8f, 19.8f, 12.1f, 19.8f)
                    curveTo(15f, 19.8f, 17.5f, 18.1f, 18.5f, 15.5f)
                    close()
                }
            }.build()
    }
}
