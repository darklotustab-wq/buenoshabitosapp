package com.healthtracker.app.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Hábito que aparece en el checklist diario.
 * isCore = true → hábitos base que vienen con la app (medicación, instrumento, etc.)
 * isCore = false → hábitos extra agregados por el usuario.
 */
@Entity(tableName = "habits")
data class Habit(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String,
    val subtitle: String = "",
    val iconKey: String,         // "pill", "run", "music", "smile", "meditation", etc.
    val colorKey: String,        // "red", "amber", "purple", "green", "blue", "teal", "pink"
    val isCore: Boolean = false,
    val orderIndex: Int = 0
)
