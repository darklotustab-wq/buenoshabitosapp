package com.healthtracker.app.data.repository

import com.healthtracker.app.data.local.AppDatabase
import com.healthtracker.app.data.model.*
import kotlinx.coroutines.flow.Flow

class HealthRepository(private val db: AppDatabase) {

    // --- Meals ---
    fun mealsForDate(date: String): Flow<List<Meal>> = db.mealDao().observeByDate(date)
    suspend fun saveMeal(meal: Meal) = db.mealDao().insert(meal)
    suspend fun getMeal(date: String, type: MealType) = db.mealDao().getByDateAndType(date, type)

    // --- Habits ---
    fun allHabits(): Flow<List<Habit>> = db.habitDao().observeAll()
    fun completionsForDate(date: String): Flow<List<HabitCompletion>> =
        db.habitDao().observeCompletions(date)

    suspend fun addHabit(habit: Habit) = db.habitDao().insert(habit)
    suspend fun deleteHabit(habit: Habit) = db.habitDao().delete(habit)

    suspend fun toggleHabit(habitId: Long, date: String, currentlyDone: Boolean) {
        if (currentlyDone) {
            db.habitDao().deleteCompletion(habitId, date)
        } else {
            db.habitDao().insertCompletion(HabitCompletion(habitId, date))
        }
    }

    // --- Water ---
    fun waterForDate(date: String): Flow<WaterIntake?> = db.waterDao().observeByDate(date)
    suspend fun setWater(date: String, glasses: Int, goal: Int = 8) {
        db.waterDao().upsert(WaterIntake(date, glasses.coerceIn(0, goal), goal))
    }

    // --- Setup inicial ---
    suspend fun seedCoreHabitsIfEmpty() {
        if (db.habitDao().count() > 0) return
        db.habitDao().insertAll(
            listOf(
                Habit(title = "Tomar medicación", subtitle = "8:00 · mañana",
                    iconKey = "pill", colorKey = "red", isCore = true, orderIndex = 0),
                Habit(title = "Actividad física", subtitle = "30 min",
                    iconKey = "run", colorKey = "amber", isCore = true, orderIndex = 1),
                Habit(title = "Practicar instrumento", subtitle = "20 min",
                    iconKey = "music", colorKey = "purple", isCore = true, orderIndex = 2),
                Habit(title = "Algo positivo", subtitle = "Escribir 1 cosa buena",
                    iconKey = "smile", colorKey = "teal", isCore = true, orderIndex = 3),
                Habit(title = "Meditación", subtitle = "10 min · respiración",
                    iconKey = "meditation", colorKey = "blue", isCore = true, orderIndex = 4)
            )
        )
    }
}
