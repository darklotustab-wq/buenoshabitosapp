package com.healthtracker.app.data.local

import androidx.room.TypeConverter
import com.healthtracker.app.data.model.MealType

class Converters {
    @TypeConverter
    fun fromMealType(value: MealType): String = value.name

    @TypeConverter
    fun toMealType(value: String): MealType = MealType.valueOf(value)
}
