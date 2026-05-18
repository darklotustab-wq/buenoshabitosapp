package com.healthtracker.app.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import com.healthtracker.app.data.repository.HealthRepository
import com.healthtracker.app.ui.dashboard.DashboardScreen
import com.healthtracker.app.ui.history.HistoryScreen
import com.healthtracker.app.ui.progress.ProgressScreen
import com.healthtracker.app.ui.settings.SettingsScreen
import com.healthtracker.app.ui.theme.HealthTrackerTheme

enum class AppTab(val title: String, val icon: ImageVector) {
    HOY("Hoy", Icons.Filled.Home),
    HISTORIAL("Historial", Icons.Filled.CalendarMonth),
    PROGRESO("Progreso", Icons.Filled.BarChart),
    AJUSTES("Ajustes", Icons.Filled.Settings)
}

@Composable
fun HealthTrackerRoot(repo: HealthRepository) {
    HealthTrackerTheme {
        var tab by rememberSaveable { mutableStateOf(AppTab.HOY) }
        Scaffold(
            containerColor = MaterialTheme.colorScheme.background,
            bottomBar = {
                NavigationBar(containerColor = Color.White) {
                    AppTab.values().forEach { t ->
                        NavigationBarItem(
                            selected = tab == t,
                            onClick = { tab = t },
                            icon = { Icon(t.icon, contentDescription = t.title) },
                            label = { Text(t.title) }
                        )
                    }
                }
            }
        ) { padding ->
            Box(modifier = Modifier.padding(padding)) {
                when (tab) {
                    AppTab.HOY -> DashboardScreen(repo)
                    AppTab.HISTORIAL -> HistoryScreen(repo)
                    AppTab.PROGRESO -> ProgressScreen(repo)
                    AppTab.AJUSTES -> SettingsScreen(repo)
                }
            }
        }
    }
}
