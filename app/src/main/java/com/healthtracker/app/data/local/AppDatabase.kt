package com.healthtracker.app.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.healthtracker.app.data.model.Habit
import com.healthtracker.app.data.model.HabitCompletion
import com.healthtracker.app.data.model.Meal
import com.healthtracker.app.data.model.SettingEntry
import com.healthtracker.app.data.model.WaterIntake

@Database(
    entities = [
        Meal::class,
        Habit::class,
        HabitCompletion::class,
        WaterIntake::class,
        SettingEntry::class
    ],
    version = 2,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun mealDao(): MealDao
    abstract fun habitDao(): HabitDao
    abstract fun waterDao(): WaterDao
    abstract fun settingsDao(): SettingsDao

    companion object {
        @Volatile private var INSTANCE: AppDatabase? = null

        private val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL(
                    "CREATE TABLE IF NOT EXISTS settings (" +
                        "`key` TEXT NOT NULL PRIMARY KEY, " +
                        "`value` TEXT NOT NULL)"
                )
            }
        }

        fun get(context: Context): AppDatabase =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "healthtracker.db"
                )
                    .addMigrations(MIGRATION_1_2)
                    .fallbackToDestructiveMigration()
                    .build()
                    .also { INSTANCE = it }
            }
    }
}
