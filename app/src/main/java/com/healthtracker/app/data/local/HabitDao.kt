package com.healthtracker.app.data.local

import androidx.room.*
import com.healthtracker.app.data.model.Habit
import com.healthtracker.app.data.model.HabitCompletion
import kotlinx.coroutines.flow.Flow

@Dao
interface HabitDao {

    @Query("SELECT * FROM habits ORDER BY sortOrder, id")
    fun observeAllHabits(): Flow<List<Habit>>

    @Query("SELECT * FROM habits ORDER BY sortOrder, id")
    suspend fun getAllHabits(): List<Habit>

    @Query("SELECT COUNT(*) FROM habits")
    suspend fun count(): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(habit: Habit): Long

    @Update
    suspend fun update(habit: Habit)

    @Query("DELETE FROM habits WHERE id = :id")
    suspend fun deleteById(id: Long)

    @Query("DELETE FROM habit_completions WHERE habitId = :habitId")
    suspend fun deleteCompletionsByHabit(habitId: Long)

    @Query("SELECT * FROM habit_completions WHERE date = :date")
    fun completionsForDate(date: String): Flow<List<HabitCompletion>>

    @Query("SELECT * FROM habit_completions WHERE date BETWEEN :start AND :end")
    suspend fun completionsBetween(start: String, end: String): List<HabitCompletion>

    @Query("SELECT * FROM habit_completions WHERE habitId = :habitId ORDER BY date")
    suspend fun completionsForHabit(habitId: Long): List<HabitCompletion>

    @Query("SELECT DISTINCT date FROM habit_completions WHERE date BETWEEN :start AND :end")
    suspend fun datesWithCompletions(start: String, end: String): List<String>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertCompletion(c: HabitCompletion)

    @Query("DELETE FROM habit_completions WHERE habitId = :habitId AND date = :date")
    suspend fun deleteCompletion(habitId: Long, date: String)

    @Query("DELETE FROM habit_completions")
    suspend fun deleteAllCompletions()

    @Query("DELETE FROM habits")
    suspend fun deleteAllHabits()
}
