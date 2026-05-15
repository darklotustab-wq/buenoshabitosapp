package com.healthtracker.app.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColors = lightColorScheme(
    primary = Color(0xFF1D9E75),
    onPrimary = Color.White,
    background = Color(0xFFF7F6F2),
    surface = Color.White,
    onSurface = Color(0xFF1A1A1A),
    onBackground = Color(0xFF1A1A1A)
)

@Composable
fun HealthTrackerTheme(content: @Composable () -> Unit) {
    MaterialTheme(colorScheme = LightColors, content = content)
}
