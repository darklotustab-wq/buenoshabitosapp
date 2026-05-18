package com.healthtracker.app.data.model

import androidx.room.Entity

@Entity(tableName = "habit_completions", primaryKeys = ["habitId", "date"])
data class HabitCompletion(
    val habitId: Long,
    val date: String, // yyyy-MM-dd
    val completedAt: Long = System.currentTimeMillis()
)
