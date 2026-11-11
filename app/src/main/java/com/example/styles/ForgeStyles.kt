package com.example.styles

import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.ui.theme.ForgeBlueLight
import com.example.ui.theme.ForgeElectricBlue
import com.example.ui.theme.ForgeVioletAccent

object ForgeStyles {
    val PrimaryGradientBrush = Brush.linearGradient(
        colors = listOf(ForgeElectricBlue, ForgeVioletAccent)
    )

    val SubtlePanelGradient = Brush.verticalGradient(
        colors = listOf(Color(0xFF171E2E), Color(0xFF101520))
    )

    val HeroOverlayBrush = Brush.verticalGradient(
        colors = listOf(
            Color(0x660A0D14),
            Color(0xCC0A0D14),
            Color(0xF50A0D14)
        )
    )

    val AccentHeaderBrush = Brush.horizontalGradient(
        colors = listOf(
            ForgeElectricBlue.copy(alpha = 0.20f),
            ForgeVioletAccent.copy(alpha = 0.14f),
            Color.Transparent
        )
    )
}

/**
 * Draws a subtle technical CAD / 3D engine blueprint grid in the background of panels.
 */
fun Modifier.engineGridBackground(
    gridColor: Color = ForgeBlueLight.copy(alpha = 0.04f),
    stepDp: Float = 28f
): Modifier = this.drawBehind {
    val stepPx = stepDp.dp.toPx()
    if (stepPx <= 0f) return@drawBehind
    var x = 0f
    while (x < size.width) {
        drawLine(
            color = gridColor,
            start = Offset(x, 0f),
            end = Offset(x, size.height),
            strokeWidth = 1f
        )
        x += stepPx
    }
    var y = 0f
    while (y < size.height) {
        drawLine(
            color = gridColor,
            start = Offset(0f, y),
            end = Offset(size.width, y),
            strokeWidth = 1f
        )
        y += stepPx
    }
}
