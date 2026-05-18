package com.healthtracker.app.data.local

import androidx.room.*
import com.healthtracker.app.data.model.Meal
import com.healthtracker.app.data.model.MealType
import kotlinx.coroutines.flow.Flow

@Dao
interface MealDao {

    @Query("SELECT * FROM meals WHERE date = :date ORDER BY type")
    fun mealsForDate(date: String): Flow<List<Meal>>

    @Query("SELECT * FROM meals WHERE date = :date AND type = :type LIMIT 1")
    suspend fun mealForDateAndType(date: String, type: MealType): Meal?

    @Query("SELECT DISTINCT date FROM meals WHERE date BETWEEN :start AND :end")
    suspend fun datesWithMeals(start: String, end: String): List<String>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(meal: Meal): Long

    @Update
    suspend fun update(meal: Meal)

    @Query("DELETE FROM meals")
    suspend fun deleteAll()
}
