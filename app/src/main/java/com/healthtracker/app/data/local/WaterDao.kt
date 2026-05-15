package com.healthtracker.app.data.local

import androidx.room.*
import com.healthtracker.app.data.model.WaterIntake
import kotlinx.coroutines.flow.Flow

@Dao
interface WaterDao {
    @Query("SELECT * FROM water_intake WHERE date = :date LIMIT 1")
    fun observeByDate(date: String): Flow<WaterIntake?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(intake: WaterIntake)
}
