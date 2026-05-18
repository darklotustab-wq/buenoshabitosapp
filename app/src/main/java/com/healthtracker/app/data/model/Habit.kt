package com.healthtracker.app.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "habits")
data class Habit(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String,
    val subtitle: String? = null,
    val iconKey: String,      // p.ej. "pill", "run", "music"...
    val colorKey: String,     // p.ej. "red", "amber"...
    val isCore: Boolean = false,
    val sortOrder: Int = 0
)
