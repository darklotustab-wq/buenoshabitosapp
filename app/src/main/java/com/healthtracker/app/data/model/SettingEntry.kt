package com.healthtracker.app.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "settings")
data class SettingEntry(
    @PrimaryKey val key: String,
    val value: String
)
