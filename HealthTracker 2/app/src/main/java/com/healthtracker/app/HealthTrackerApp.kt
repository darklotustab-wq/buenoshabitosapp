package com.healthtracker.app

import android.app.Application
import com.healthtracker.app.data.local.AppDatabase
import com.healthtracker.app.data.repository.HealthRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

class HealthTrackerApp : Application() {
    lateinit var repository: HealthRepository
        private set

    override fun onCreate() {
        super.onCreate()
        val db = AppDatabase.get(this)
        repository = HealthRepository(db)

        // Inicializar hábitos base la primera vez
        CoroutineScope(Dispatchers.IO + SupervisorJob()).launch {
            repository.seedCoreHabitsIfEmpty()
        }
    }
}
