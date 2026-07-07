package io.github.androidpoet.liquidkit.sample.showcase

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.github.androidpoet.liquidkit.button.LiquidButton
import io.github.androidpoet.liquidkit.button.LiquidButtonVariant
import io.github.androidpoet.liquidkit.button.LiquidFab
import io.github.androidpoet.liquidkit.icon.LiquidIcon

private val PlusIcon: ImageVector by lazy {
    ImageVector
        .Builder(
            name = "Plus",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f,
        ).apply {
            path(fill = SolidColor(Color.White)) {
                moveTo(11f, 5f)
                horizontalLineTo(13f)
                verticalLineTo(11f)
                horizontalLineTo(19f)
                verticalLineTo(13f)
                horizontalLineTo(13f)
                verticalLineTo(19f)
                horizontalLineTo(11f)
                verticalLineTo(13f)
                horizontalLineTo(5f)
                verticalLineTo(11f)
                horizontalLineTo(11f)
                close()
            }
        }.build()
}

/**
 * Self-contained showcase for [LiquidButton] (both variants) and [LiquidFab].
 *
 * Renders Android shader glass on Android and native UIButton glass on iOS.
 */
@Composable
public fun ButtonShowcase() {
    var clicks by remember { mutableStateOf(0) }

    val fabIcon =
        remember {
            LiquidIcon(
                imageVector = PlusIcon,
                contentDescription = "Add",
                iosSystemName = "plus",
            )
        }

    Column(
        modifier =
            Modifier
                .fillMaxWidth()
                .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp),
    ) {
        BasicText(
            text = "Liquid Glass Buttons",
            style =
                TextStyle(
                    color = Color(0xFF101418),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                ),
        )

        BasicText(
            text = "Taps: $clicks",
            style = TextStyle(color = Color(0xFF4E5968), fontSize = 14.sp),
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            LiquidButton(
                text = "Glass",
                onClick = { clicks++ },
                variant = LiquidButtonVariant.Glass,
            )
            LiquidButton(
                text = "Prominent",
                onClick = { clicks++ },
                variant = LiquidButtonVariant.GlassProminent,
            )
        }

        LiquidButton(
            text = "Disabled",
            onClick = { clicks++ },
            variant = LiquidButtonVariant.GlassProminent,
            enabled = false,
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            LiquidFab(
                icon = fabIcon,
                onClick = { clicks++ },
                variant = LiquidButtonVariant.Glass,
            )
            LiquidFab(
                icon = fabIcon,
                onClick = { clicks++ },
                variant = LiquidButtonVariant.GlassProminent,
            )
        }
    }
}
