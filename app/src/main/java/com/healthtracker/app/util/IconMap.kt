package com.healthtracker.app.util

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector

object IconMap {
    fun icon(key: String): ImageVector = when (key) {
        "pill" -> Icons.Filled.Medication
        "run" -> Icons.Filled.DirectionsRun
        "music" -> Icons.Filled.MusicNote
        "smile" -> Icons.Filled.EmojiEmotions
        "meditation" -> Icons.Filled.SelfImprovement
        "book" -> Icons.Filled.MenuBook
        "water" -> Icons.Filled.LocalDrink
        "sleep" -> Icons.Filled.Bedtime
        "heart" -> Icons.Filled.Favorite
        else -> Icons.Filled.CheckCircle
    }
    val allKeys = listOf("pill", "run", "music", "smile", "meditation", "book", "water", "sleep", "heart")
}

data class HabitColors(val bg: Color, val fg: Color)

object ColorMap {
    fun colors(key: String): HabitColors = when (key) {
        "red" -> HabitColors(Color(0xFFFCEBEB), Color(0xFFA32D2D))
        "amber" -> HabitColors(Color(0xFFFAEEDA), Color(0xFFBA7517))
        "purple" -> HabitColors(Color(0xFFEEEDFE), Color(0xFF534AB7))
        "teal" -> HabitColors(Color(0xFFE1F5EE), Color(0xFF0F6E56))
        "blue" -> HabitColors(Color(0xFFE6F1FB), Color(0xFF185FA5))
        "green" -> HabitColors(Color(0xFFEAF3DE), Color(0xFF3B6D11))
        "pink" -> HabitColors(Color(0xFFFBEAF0), Color(0xFF993556))
        "coral" -> HabitColors(Color(0xFFFAECE7), Color(0xFF993C1D))
        else -> HabitColors(Color(0xFFE6F1FB), Color(0xFF185FA5))
    }
    val allKeys = listOf("red", "amber", "purple", "teal", "blue", "green", "pink", "coral")
}
