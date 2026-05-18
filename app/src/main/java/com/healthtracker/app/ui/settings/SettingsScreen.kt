package com.healthtracker.app.ui.settings

import android.app.TimePickerDialog
import android.os.Build
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.healthtracker.app.data.model.Habit
import com.healthtracker.app.data.repository.HealthRepository
import com.healthtracker.app.notifications.ReminderRegistry
import com.healthtracker.app.util.ColorMap
import com.healthtracker.app.util.IconMap
import com.healthtracker.app.viewmodel.SettingsViewModel

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun SettingsScreen(repo: HealthRepository) {
    val context = LocalContext.current
    val vm: SettingsViewModel = viewModel(
        factory = SettingsViewModel.Factory(repo, context.applicationContext)
    )
    val state by vm.state.collectAsState()
    val habits by vm.habits.collectAsState()

    var editingHabit by remember { mutableStateOf<Habit?>(null) }
    var confirmWipe by remember { mutableStateOf(false) }

    val notifPermission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        rememberPermissionState(android.Manifest.permission.POST_NOTIFICATIONS)
    } else null

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        Text(
            "Ajustes",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )
        Spacer(Modifier.height(16.dp))

        // Water goal
        SectionCard(title = "Agua") {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("Objetivo diario", modifier = Modifier.weight(1f))
                IconButton(onClick = { vm.setWaterGoal(state.waterGoal - 1) }) {
                    Icon(Icons.Filled.Remove, contentDescription = "Menos")
                }
                Text(
                    "${state.waterGoal} vasos",
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 16.sp
                )
                IconButton(onClick = { vm.setWaterGoal(state.waterGoal + 1) }) {
                    Icon(Icons.Filled.Add, contentDescription = "Más")
                }
            }
        }

        Spacer(Modifier.height(12.dp))

        // Reminders
        SectionCard(title = "Recordatorios") {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    "Activar notificaciones",
                    modifier = Modifier.weight(1f)
                )
                Switch(
                    checked = state.remindersEnabled,
                    onCheckedChange = { wanted ->
                        if (wanted && notifPermission != null && !notifPermission.status.isGranted) {
                            notifPermission.launchPermissionRequest()
                        }
                        vm.setRemindersEnabled(wanted)
                    }
                )
            }

            if (state.remindersEnabled) {
                Spacer(Modifier.height(8.dp))
                ReminderRegistry.all.forEach { entry ->
                    val time = state.reminderTimes[entry.key] ?: entry.defaultTime
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                val parts = time.split(":")
                                val h = parts.getOrNull(0)?.toIntOrNull() ?: 8
                                val m = parts.getOrNull(1)?.toIntOrNull() ?: 0
                                TimePickerDialog(
                                    context,
                                    { _, hh, mm ->
                                        val newTime = "%02d:%02d".format(hh, mm)
                                        vm.setReminderTime(entry.key, newTime)
                                    },
                                    h, m, true
                                ).show()
                            }
                            .padding(vertical = 10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(entry.title, modifier = Modifier.weight(1f))
                        Text(time, fontWeight = FontWeight.SemiBold, color = MaterialTheme.colorScheme.primary)
                        Icon(Icons.Filled.ChevronRight, contentDescription = null, tint = Color.Gray)
                    }
                    Divider(color = Color(0xFFEFEFEF))
                }
            }
        }

        Spacer(Modifier.height(12.dp))

        // Habits manager
        SectionCard(title = "Hábitos") {
            if (habits.isEmpty()) {
                Text("Sin hábitos aún.", color = Color.Gray)
            } else {
                habits.forEach { h ->
                    val colors = ColorMap.colors(h.colorKey)
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { editingHabit = h }
                            .padding(vertical = 10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(colors.bg),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = IconMap.icon(h.iconKey),
                                contentDescription = h.title,
                                tint = colors.fg,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                        Spacer(Modifier.width(12.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(h.title, fontWeight = FontWeight.SemiBold)
                            if (h.isCore) {
                                Text("Hábito base", fontSize = 11.sp, color = Color.Gray)
                            }
                        }
                        Icon(Icons.Filled.ChevronRight, contentDescription = null, tint = Color.Gray)
                    }
                    Divider(color = Color(0xFFEFEFEF))
                }
            }
        }

        Spacer(Modifier.height(12.dp))

        // Danger zone
        SectionCard(title = "Datos") {
            TextButton(
                onClick = { confirmWipe = true },
                colors = ButtonDefaults.textButtonColors(
                    contentColor = MaterialTheme.colorScheme.error
                )
            ) {
                Icon(Icons.Filled.Delete, contentDescription = null)
                Spacer(Modifier.width(8.dp))
                Text("Borrar todos mis datos")
            }
        }

        Spacer(Modifier.height(24.dp))

        Text(
            "HealthTracker v1.1.0",
            fontSize = 12.sp,
            color = Color.Gray,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )

        Spacer(Modifier.height(80.dp))
    }

    editingHabit?.let { h ->
        EditHabitDialog(
            habit = h,
            onDismiss = { editingHabit = null },
            onSave = {
                vm.updateHabit(it)
                editingHabit = null
            },
            onDelete = if (!h.isCore) {
                { vm.deleteHabit(h.id); editingHabit = null }
            } else null
        )
    }

    if (confirmWipe) {
        AlertDialog(
            onDismissRequest = { confirmWipe = false },
            title = { Text("¿Borrar todo?") },
            text = { Text("Se eliminarán fotos de comidas, hábitos, registros de agua, ajustes y recordatorios. Esta acción no se puede deshacer.") },
            confirmButton = {
                TextButton(
                    onClick = {
                        confirmWipe = false
                        vm.wipeAll()
                    }
                ) {
                    Text("Borrar todo", color = MaterialTheme.colorScheme.error)
                }
            },
            dismissButton = {
                TextButton(onClick = { confirmWipe = false }) { Text("Cancelar") }
            }
        )
    }
}

@Composable
private fun SectionCard(title: String, content: @Composable ColumnScope.() -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(title, fontWeight = FontWeight.SemiBold, fontSize = 13.sp, color = Color.Gray)
            Spacer(Modifier.height(8.dp))
            content()
        }
    }
}
