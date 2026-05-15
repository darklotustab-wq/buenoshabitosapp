package com.healthtracker.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.healthtracker.app.ui.dashboard.DashboardScreen
import com.healthtracker.app.ui.theme.HealthTrackerTheme
import com.healthtracker.app.viewmodel.DashboardViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val repo = (application as HealthTrackerApp).repository

        setContent {
            HealthTrackerTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    val vm: DashboardViewModel = viewModel(
                        factory = DashboardViewModel.Factory(repo)
                    )
                    DashboardScreen(vm)
                }
            }
        }
    }
}
