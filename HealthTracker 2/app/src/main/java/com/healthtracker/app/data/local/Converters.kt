package com.healthtracker.app.data.local

import androidx.room.TypeConverter
import com.healthtracker.app.data.model.MealType

class Converters {
    @TypeConverter fun fromMealType(t: MealType): String = t.name
    @TypeConverter fun toMealType(s: String): MealType = MealType.valueOf(s)
}
