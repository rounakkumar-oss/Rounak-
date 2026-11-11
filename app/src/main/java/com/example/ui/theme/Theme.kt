package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val WorldForgeDarkColorScheme = darkColorScheme(
    primary = ForgeElectricBlue,
    onPrimary = Color.White,
    primaryContainer = Color(0xFF1E3A8A),
    onPrimaryContainer = Color(0xFFDBEAFE),
    secondary = ForgeVioletAccent,
    onSecondary = Color.White,
    secondaryContainer = Color(0xFF3B1F71),
    onSecondaryContainer = Color(0xFFEDE9FE),
    tertiary = ForgeCyanAccent,
    onTertiary = Color(0xFF042F2E),
    background = ForgeCharcoalBg,
    onBackground = ForgeTextPrimary,
    surface = ForgeSurface,
    onSurface = ForgeTextPrimary,
    surfaceVariant = ForgeSurfaceElevated,
    onSurfaceVariant = ForgeTextSecondary,
    outline = ForgeCardBorder,
    outlineVariant = Color(0xFF1E293B),
    error = ForgeDangerRed,
    onError = Color.White
)

@Composable
fun MyApplicationTheme(
    content: @Composable () -> Unit
) {
    // World Forge AI is always rendered in its signature dark professional engine environment
    MaterialTheme(
        colorScheme = WorldForgeDarkColorScheme,
        typography = Typography,
        content = content
    )
}
