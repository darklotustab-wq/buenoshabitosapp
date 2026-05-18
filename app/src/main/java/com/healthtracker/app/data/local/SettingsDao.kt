package com.healthtracker.app.data.local

import androidx.room.*
import com.healthtracker.app.data.model.SettingEntry
import kotlinx.coroutines.flow.Flow

@Dao
interface SettingsDao {

    @Query("SELECT * FROM settings WHERE `key` = :key LIMIT 1")
    suspend fun get(key: String): SettingEntry?

    @Query("SELECT * FROM settings WHERE `key` = :key LIMIT 1")
    fun observe(key: String): Flow<SettingEntry?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(entry: SettingEntry)

    @Query("DELETE FROM settings")
    suspend fun deleteAll()
}
