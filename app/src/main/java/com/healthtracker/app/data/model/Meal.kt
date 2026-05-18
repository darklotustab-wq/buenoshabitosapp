package com.healthtracker.app.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "meals")
data class Meal(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val date: String, // yyyy-MM-dd
    val type: MealType,
    val photoPath: String? = null,
    val caption: String? = null,
    val createdAt: Long = System.currentTimeMillis()
)
