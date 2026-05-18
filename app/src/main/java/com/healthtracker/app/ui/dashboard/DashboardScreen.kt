package com.healthtracker.app.ui.dashboard

import android.app.Activity
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.healthtracker.app.data.model.Meal
import com.healthtracker.app.data.model.MealType
import com.healthtracker.app.data.repository.HealthRepository
import com.healthtracker.app.ui.components.*
import com.healthtracker.app.util.PhotoUtils
import com.healthtracker.app.viewmodel.DashboardViewModel

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun DashboardScreen(repo: HealthRepository) {
    val vm: DashboardViewModel = viewModel(factory = DashboardViewModel.Factory(repo))
    val context = LocalContext.current

    val habits by vm.habits.collectAsState()
    val meals by vm.todayMeals.collectAsState()
    val completions by vm.todayCompletions.collectAsState()
    val water by vm.water.collectAsState()
    val goal by vm.waterGoal.collectAsState()

    var pendingType by remember { mutableStateOf<MealType?>(null) }
    var pendingPath by remember { mutableStateOf<String?>(null) }
    var showAddHabit by remember { mutableStateOf(false) }
    var viewingMeal by remember { mutableStateOf<Meal?>(null) }

    val cameraPermission = rememberPermissionState(android.Manifest.permission.CAMERA)

    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { success ->
        val t = pendingType
        val p = pendingPath
        if (success && t != null && p != null) {
            vm.saveMealPhoto(t, p)
        }
        pendingType = null
        pendingPath = null
    }

    fun launchCameraFor(type: MealType) {
        val (uri, path) = PhotoUtils.createMealPhotoUri(context)
        pendingType = type
        pendingPath = path
        cameraLauncher.launch(uri)
    }

    val mealsByType = meals.associateBy { it.type }
    val completedIds = completions.map { it.habitId }.toSet()

    val totalSteps = MealType.values().size + habits.size + 1 // meals + habits + water
    val completedSteps =
        MealType.values().count { mealsByType[it]?.photoPath != null } +
        completedIds.size +
        (if ((water?.glasses ?: 0) >= goal && goal > 0) 1 else 0)
    val progress = if (totalSteps == 0) 0f else completedSteps / totalSteps.toFloat()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        DayHeader(progress = progress)
        Spacer(Modifier.height(16.dp))

        // Meals grid 2x2
        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            val types = MealType.values()
            for (i in 0 until types.size step 2) {
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    Box(modifier = Modifier.weight(1f)) {
                        val t = types[i]
                        MealCard(
                            type = t,
                            meal = mealsByType[t],
                            onAddPhotoClick = {
                                if (cameraPermission.status.isGranted) launchCameraFor(t)
                                else cameraPermission.launchPermissionRequest()
                            },
                            onViewPhotoClick = { viewingMeal = it }
                        )
                    }
                    if (i + 1 < types.size) {
                        Box(modifier = Modifier.weight(1f)) {
                            val t = types[i + 1]
                            MealCard(
                                type = t,
                                meal = mealsByType[t],
                                onAddPhotoClick = {
                                    if (cameraPermission.status.isGranted) launchCameraFor(t)
                                    else cameraPermission.launchPermissionRequest()
                                },
                                onViewPhotoClick = { viewingMeal = it }
                            )
                        }
                    }
                }
            }
        }

        Spacer(Modifier.height(16.dp))

        WaterTracker(
            glasses = water?.glasses ?: 0,
            goal = goal,
            onGlassClick = { vm.setGlasses(it) }
        )

        Spacer(Modifier.height(16.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                "Hábitos",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(Modifier.weight(1f))
            IconButton(onClick = { showAddHabit = true }) {
                Icon(Icons.Filled.Add, contentDescription = "Agregar hábito")
            }
        }

        Spacer(Modifier.height(8.dp))

        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            habits.forEach { habit ->
                HabitRow(
                    habit = habit,
                    completed = completedIds.contains(habit.id),
                    onToggle = { vm.toggleHabit(habit, it) }
                )
            }
        }

        Spacer(Modifier.height(80.dp))
    }

    if (showAddHabit) {
        AddHabitDialog(
            onDismiss = { showAddHabit = false },
            onConfirm = { title, icon, color ->
                vm.addCustomHabit(title, icon, color)
                showAddHabit = false
            }
        )
    }

    viewingMeal?.let { meal ->
        PhotoViewer(meal = meal, onDismiss = { viewingMeal = null })
    }
}
