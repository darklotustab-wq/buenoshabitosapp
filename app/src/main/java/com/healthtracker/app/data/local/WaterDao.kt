package com.healthtracker.app.data.local

import androidx.room.*
import com.healthtracker.app.data.model.WaterIntake
import kotlinx.coroutines.flow.Flow

@Dao
interface WaterDao {

    @Query("SELECT * FROM water_intake WHERE date = :date LIMIT 1")
    fun waterForDate(date: String): Flow<WaterIntake?>

    @Query("SELECT * FROM water_intake WHERE date = :date LIMIT 1")
    suspend fun waterForDateOnce(date: String): WaterIntake?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(water: WaterIntake)

    @Query("DELETE FROM water_intake")
    suspend fun deleteAll()
}
