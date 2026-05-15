package com.healthtracker.app.ui.dashboard

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.healthtracker.app.data.model.MealType
import com.healthtracker.app.ui.components.*
import com.healthtracker.app.util.PhotoUtils
import com.healthtracker.app.viewmodel.DashboardViewModel

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun DashboardScreen(viewModel: DashboardViewModel) {
    val state by viewModel.state.collectAsState()
    val context = LocalContext.current

    // Estado para la cámara
    var pendingMealType by rememberSaveable { mutableStateOf<MealType?>(null) }
    var pendingPhotoPath by rememberSaveable { mutableStateOf<String?>(null) }
    var pendingPhotoUri by remember { mutableStateOf<Uri?>(null) }
    var showAddHabit by remember { mutableStateOf(false) }

    val cameraPermission = rememberPermissionState(android.Manifest.permission.CAMERA)

    val takePicture = rememberLauncherForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { success ->
        if (success) {
            val type = pendingMealType
            val path = pendingPhotoPath
            if (type != null && path != null) {
                viewModel.saveMealPhoto(type, path)
            }
        }
        pendingMealType = null
        pendingPhotoPath = null
        pendingPhotoUri = null
    }

    fun launchCameraFor(type: MealType) {
        val (uri, path) = PhotoUtils.createMealPhotoUri(context)
        pendingMealType = type
        pendingPhotoPath = path
        pendingPhotoUri = uri
        takePicture.launch(uri)
    }

    Column(
        Modifier
            .fillMaxSize()
            .background(Color(0xFFF7F6F2))
            .verticalScroll(rememberScrollState())
    ) {
        DayHeader(state)

        // --- Sección Alimentación ---
        Column(Modifier.padding(20.dp, 20.dp, 20.dp, 0.dp)) {
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Alimentación", fontSize = 16.sp, fontWeight = FontWeight.Medium)
                Text("${state.mealsLogged} de 4 comidas",
                    fontSize = 12.sp, color = Color(0xFF888780))
            }
            Spacer(Modifier.height(12.dp))

            // Grid 2x2
            val meals = state.mealsByType
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    MealCard(MealType.BREAKFAST, meals[MealType.BREAKFAST],
                        onAddPhotoClick = {
                            if (cameraPermission.status.isGranted) launchCameraFor(MealType.BREAKFAST)
                            else cameraPermission.launchPermissionRequest()
                        },
                        modifier = Modifier.weight(1f))
                    MealCard(MealType.LUNCH, meals[MealType.LUNCH],
                        onAddPhotoClick = {
                            if (cameraPermission.status.isGranted) launchCameraFor(MealType.LUNCH)
                            else cameraPermission.launchPermissionRequest()
                        },
                        modifier = Modifier.weight(1f))
                }
                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    MealCard(MealType.SNACK, meals[MealType.SNACK],
                        onAddPhotoClick = {
                            if (cameraPermission.status.isGranted) launchCameraFor(MealType.SNACK)
                            else cameraPermission.launchPermissionRequest()
                        },
                        modifier = Modifier.weight(1f))
                    MealCard(MealType.DINNER, meals[MealType.DINNER],
                        onAddPhotoClick = {
                            if (cameraPermission.status.isGranted) launchCameraFor(MealType.DINNER)
                            else cameraPermission.launchPermissionRequest()
                        },
                        modifier = Modifier.weight(1f))
                }
            }

            Spacer(Modifier.height(10.dp))
            WaterTracker(
                glasses = state.waterGlasses,
                goal = state.waterGoal,
                onAdd = viewModel::addWater,
                onRemove = viewModel::removeWater
            )
        }

        // --- Sección Hábitos ---
        Column(Modifier.padding(20.dp, 24.dp, 20.dp, 0.dp)) {
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Hábitos del día", fontSize = 16.sp, fontWeight = FontWeight.Medium)
                TextButton(onClick = { showAddHabit = true }) {
                    Icon(Icons.Filled.Add, null, modifier = Modifier.size(16.dp))
                    Spacer(Modifier.width(2.dp))
                    Text("Agregar", fontSize = 13.sp)
                }
            }
            Spacer(Modifier.height(4.dp))

            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                state.habits.forEach { habit ->
                    HabitRow(
                        habit = habit,
                        done = state.completedHabitIds.contains(habit.id),
                        onToggle = { viewModel.toggleHabit(habit) },
                        onDelete = { viewModel.deleteHabit(habit) }
                    )
                }
                if (state.habits.isEmpty()) {
                    Text("Cargando hábitos...", color = Color(0xFF888780), fontSize = 13.sp)
                }
            }
        }

        Spacer(Modifier.height(32.dp))

        // --- Bottom nav decorativa ---
        Row(
            Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(Color.White)
                .border(0.5.dp, Color(0xFFEDEBE3), RoundedCornerShape(12.dp))
                .padding(10.dp),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            NavTab(Icons.Filled.Home, "Hoy", true)
            NavTab(Icons.Filled.CalendarMonth, "Historial", false)
            NavTab(Icons.Filled.BarChart, "Progreso", false)
            NavTab(Icons.Filled.Settings, "Ajustes", false)
        }

        Spacer(Modifier.height(8.dp))
    }

    if (showAddHabit) {
        AddHabitDialog(
            onDismiss = { showAddHabit = false },
            onConfirm = { title, subtitle, iconKey, colorKey ->
                viewModel.addCustomHabit(title, subtitle, iconKey, colorKey)
                showAddHabit = false
            }
        )
    }
}

@Composable
private fun NavTab(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    selected: Boolean
) {
    val color = if (selected) Color(0xFF185FA5) else Color(0xFF888780)
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Icon(icon, label, tint = color, modifier = Modifier.size(22.dp))
        Spacer(Modifier.height(2.dp))
        Text(label, fontSize = 10.sp, color = color,
            fontWeight = if (selected) FontWeight.Medium else FontWeight.Normal)
    }
}
