package com.healthtracker.app.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "water_intake")
data class WaterIntake(
    @PrimaryKey val date: String, // yyyy-MM-dd
    val glasses: Int = 0
)
