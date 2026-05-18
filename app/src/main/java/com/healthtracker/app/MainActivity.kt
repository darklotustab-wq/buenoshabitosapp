package com.healthtracker.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.healthtracker.app.data.local.AppDatabase
import com.healthtracker.app.data.repository.HealthRepository
import com.healthtracker.app.ui.HealthTrackerRoot

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val db = AppDatabase.get(applicationContext)
        val repo = HealthRepository(db)
        setContent {
            HealthTrackerRoot(repo)
        }
    }
}
