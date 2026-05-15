package com.healthtracker.app.data.local

import androidx.room.*
import com.healthtracker.app.data.model.Meal
import com.healthtracker.app.data.model.MealType
import kotlinx.coroutines.flow.Flow

@Dao
interface MealDao {
    @Query("SELECT * FROM meals WHERE date = :date ORDER BY type")
    fun observeByDate(date: String): Flow<List<Meal>>

    @Query("SELECT * FROM meals WHERE date = :date AND type = :type LIMIT 1")
    suspend fun getByDateAndType(date: String, type: MealType): Meal?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(meal: Meal): Long

    @Update
    suspend fun update(meal: Meal)

    @Delete
    suspend fun delete(meal: Meal)
}
