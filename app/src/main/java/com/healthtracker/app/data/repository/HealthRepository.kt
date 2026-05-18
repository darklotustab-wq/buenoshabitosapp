package com.healthtracker.app.data.repository

import com.healthtracker.app.data.local.AppDatabase
import com.healthtracker.app.data.model.*
import kotlinx.coroutines.flow.Flow

class HealthRepository(private val db: AppDatabase) {

    private val mealDao = db.mealDao()
    private val habitDao = db.habitDao()
    private val waterDao = db.waterDao()
    private val settingsDao = db.settingsDao()

    // --- meals ---
    fun mealsForDate(date: String): Flow<List<Meal>> = mealDao.mealsForDate(date)

    suspend fun saveMealPhoto(date: String, type: MealType, photoPath: String) {
        val existing = mealDao.mealForDateAndType(date, type)
        if (existing != null) {
            mealDao.update(existing.copy(photoPath = photoPath))
        } else {
            mealDao.insert(Meal(date = date, type = type, photoPath = photoPath))
        }
    }

    suspend fun mealForDateAndType(date: String, type: MealType): Meal? =
        mealDao.mealForDateAndType(date, type)

    // --- habits ---
    fun observeHabits(): Flow<List<Habit>> = habitDao.observeAllHabits()
    suspend fun getHabits(): List<Habit> = habitDao.getAllHabits()
    suspend fun habitCount(): Int = habitDao.count()
    suspend fun upsertHabit(habit: Habit) { habitDao.upsert(habit) }
    suspend fun updateHabit(habit: Habit) { habitDao.update(habit) }
    suspend fun deleteHabit(id: Long) {
        habitDao.deleteCompletionsByHabit(id)
        habitDao.deleteById(id)
    }

    // --- completions ---
    fun completionsForDate(date: String): Flow<List<HabitCompletion>> =
        habitDao.completionsForDate(date)

    suspend fun completionsBetween(start: String, end: String): List<HabitCompletion> =
        habitDao.completionsBetween(start, end)

    suspend fun completionsForHabit(habitId: Long): List<HabitCompletion> =
        habitDao.completionsForHabit(habitId)

    suspend fun toggleCompletion(habitId: Long, date: String, completed: Boolean) {
        if (completed) habitDao.insertCompletion(HabitCompletion(habitId, date))
        else habitDao.deleteCompletion(habitId, date)
    }

    // --- water ---
    fun waterForDate(date: String): Flow<WaterIntake?> = waterDao.waterForDate(date)

    suspend fun setGlasses(date: String, glasses: Int) {
        waterDao.upsert(WaterIntake(date, glasses.coerceAtLeast(0)))
    }

    // --- dates with activity (para calendario) ---
    suspend fun datesWithAnyActivity(start: String, end: String): Set<String> {
        val a = mealDao.datesWithMeals(start, end)
        val b = habitDao.datesWithCompletions(start, end)
        return (a + b).toSet()
    }

    // --- settings ---
    suspend fun getSetting(key: String): String? = settingsDao.get(key)?.value
    fun observeSetting(key: String): Flow<String?> =
        kotlinx.coroutines.flow.flow<String?> {
            settingsDao.observe(key).collect { entry -> emit(entry?.value) }
        }

    suspend fun setSetting(key: String, value: String) {
        settingsDao.upsert(SettingEntry(key, value))
    }

    // --- wipe ---
    suspend fun wipeAllData() {
        mealDao.deleteAll()
        habitDao.deleteAllCompletions()
        habitDao.deleteAllHabits()
        waterDao.deleteAll()
        settingsDao.deleteAll()
    }
}
