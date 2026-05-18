package com.healthtracker.app.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColors = lightColorScheme(
    primary = Color(0xFF1D9E75),
    onPrimary = Color.White,
    secondary = Color(0xFF185FA5),
    background = Color(0xFFF7F6F2),
    surface = Color.White,
    onSurface = Color(0xFF1B1B1B)
)

@Composable
fun HealthTrackerTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = LightColors,
        content = content
    )
}
