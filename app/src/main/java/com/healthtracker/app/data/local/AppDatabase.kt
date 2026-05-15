package com.healthtracker.app.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.healthtracker.app.data.model.Habit
import com.healthtracker.app.data.model.HabitCompletion
import com.healthtracker.app.data.model.Meal
import com.healthtracker.app.data.model.WaterIntake

@Database(
    entities = [Meal::class, Habit::class, HabitCompletion::class, WaterIntake::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun mealDao(): MealDao
    abstract fun habitDao(): HabitDao
    abstract fun waterDao(): WaterDao

    companion object {
        @Volatile private var INSTANCE: AppDatabase? = null

        fun get(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "healthtracker.db"
                ).build().also { INSTANCE = it }
            }
        }
    }
}
