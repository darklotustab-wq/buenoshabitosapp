package com.healthtracker.app.data.model

import androidx.room.Entity
import androidx.room.Index

/**
 * Registro de un hábito completado en una fecha específica.
 * Si existe la fila → hábito hecho ese día.
 */
@Entity(
    tableName = "habit_completions",
    primaryKeys = ["habitId", "date"],
    indices = [Index("date")]
)
data class HabitCompletion(
    val habitId: Long,
    val date: String,            // yyyy-MM-dd
    val completedAt: Long = System.currentTimeMillis(),
    val note: String? = null     // por ej. para "algo positivo" del día
)
