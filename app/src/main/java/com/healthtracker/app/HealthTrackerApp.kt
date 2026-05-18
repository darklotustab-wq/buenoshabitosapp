package com.healthtracker.app

import android.app.Application
import com.healthtracker.app.data.local.AppDatabase
import com.healthtracker.app.data.model.Habit
import com.healthtracker.app.data.repository.HealthRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

class HealthTrackerApp : Application() {

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    override fun onCreate() {
        super.onCreate()
        val db = AppDatabase.get(this)
        val repo = HealthRepository(db)
        scope.launch { seedCoreHabitsIfEmpty(repo) }
    }

    private suspend fun seedCoreHabitsIfEmpty(repo: HealthRepository) {
        if (repo.habitCount() > 0) return
        val core = listOf(
            Habit(title = "Medicación",       subtitle = "Tomar a horario", iconKey = "pill",       colorKey = "red",    isCore = true, sortOrder = 1),
            Habit(title = "Actividad física", subtitle = "30 minutos",      iconKey = "run",        colorKey = "amber",  isCore = true, sortOrder = 2),
            Habit(title = "Instrumento",      subtitle = "Practicar",        iconKey = "music",      colorKey = "purple", isCore = true, sortOrder = 3),
            Habit(title = "Algo positivo",    subtitle = "Anotar 1 cosa",   iconKey = "smile",      colorKey = "green",  isCore = true, sortOrder = 4),
            Habit(title = "Meditación",       subtitle = "10 minutos",       iconKey = "meditation", colorKey = "blue",   isCore = true, sortOrder = 5)
        )
        core.forEach { repo.upsertHabit(it) }
    }
}
