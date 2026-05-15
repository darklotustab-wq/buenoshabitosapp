package com.healthtracker.app.util

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector

/**
 * Mapeo de claves de string (que se guardan en DB) a iconos y colores Compose.
 * Permite editar hábitos sin perder referencias.
 */
object IconMap {
    fun icon(key: String): ImageVector = when (key) {
        "pill"       -> Icons.Filled.Medication
        "run"        -> Icons.Filled.DirectionsRun
        "music"      -> Icons.Filled.MusicNote
        "smile"      -> Icons.Filled.SentimentVerySatisfied
        "meditation" -> Icons.Filled.SelfImprovement
        "book"       -> Icons.Filled.MenuBook
        "water"      -> Icons.Filled.WaterDrop
        "sleep"      -> Icons.Filled.Bedtime
        "heart"      -> Icons.Filled.Favorite
        else         -> Icons.Filled.CheckCircle
    }
}

/** Pareja de color de fondo (suave) y color de ícono (oscuro). */
data class HabitColors(val background: Color, val foreground: Color)

object ColorMap {
    fun colors(key: String): HabitColors = when (key) {
        "red"    -> HabitColors(Color(0xFFFCEBEB), Color(0xFFA32D2D))
        "amber"  -> HabitColors(Color(0xFFFAEEDA), Color(0xFFBA7517))
        "purple" -> HabitColors(Color(0xFFEEEDFE), Color(0xFF534AB7))
        "teal"   -> HabitColors(Color(0xFFE1F5EE), Color(0xFF0F6E56))
        "blue"   -> HabitColors(Color(0xFFE6F1FB), Color(0xFF185FA5))
        "green"  -> HabitColors(Color(0xFFEAF3DE), Color(0xFF3B6D11))
        "pink"   -> HabitColors(Color(0xFFFBEAF0), Color(0xFF993556))
        "coral"  -> HabitColors(Color(0xFFFAECE7), Color(0xFF993C1D))
        else     -> HabitColors(Color(0xFFF1EFE8), Color(0xFF5F5E5A))
    }
    val available = listOf("red","amber","purple","teal","blue","green","pink","coral")
}
