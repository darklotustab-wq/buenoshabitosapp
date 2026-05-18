package com.healthtracker.app.ui.history

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.healthtracker.app.data.model.Meal
import com.healthtracker.app.data.model.MealType
import com.healthtracker.app.data.repository.HealthRepository
import com.healthtracker.app.ui.components.PhotoViewer
import com.healthtracker.app.util.ColorMap
import com.healthtracker.app.util.DateUtils
import com.healthtracker.app.util.IconMap
import com.healthtracker.app.viewmodel.HistoryViewModel
import java.io.File
import java.util.Calendar

@Composable
fun HistoryScreen(repo: HealthRepository) {
    val vm: HistoryViewModel = viewModel(factory = HistoryViewModel.Factory(repo))
    val month by vm.month.collectAsState()
    val selectedDate by vm.selectedDate.collectAsState()
    val activityDates by vm.activityDates.collectAsState()
    val summary by vm.daySummary.collectAsState()

    var viewingMeal by remember { mutableStateOf<Meal?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        Text(
            "Historial",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )
        Spacer(Modifier.height(12.dp))

        // Month nav
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = { vm.changeMonth(-1) }) {
                Icon(Icons.Filled.ChevronLeft, contentDescription = "Mes anterior")
            }
            Text(
                DateUtils.displayMonthYear(month),
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.weight(1f),
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )
            IconButton(onClick = { vm.changeMonth(1) }) {
                Icon(Icons.Filled.ChevronRight, contentDescription = "Mes siguiente")
            }
        }

        Spacer(Modifier.height(8.dp))

        // Weekday headers (Lun..Dom)
        Row {
            listOf("L", "M", "M", "J", "V", "S", "D").forEach { d ->
                Text(
                    d,
                    modifier = Modifier.weight(1f),
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                    color = Color.Gray,
                    fontSize = 12.sp
                )
            }
        }
        Spacer(Modifier.height(4.dp))

        // Grid 6x7
        val grid = DateUtils.monthGrid(month)
        for (row in 0 until 6) {
            Row {
                for (col in 0 until 7) {
                    val idx = row * 7 + col
                    val date = grid[idx]
                    Box(modifier = Modifier.weight(1f)) {
                        if (date != null) {
                            DayCell(
                                date = date,
                                hasActivity = activityDates.contains(date),
                                isSelected = date == selectedDate,
                                onClick = { vm.selectDate(date) }
                            )
                        } else {
                            Box(modifier = Modifier.size(40.dp))
                        }
                    }
                }
            }
        }

        Spacer(Modifier.height(20.dp))

        // Day summary
        DaySummaryView(
            date = summary.date,
            meals = summary.meals,
            completedHabitIds = summary.completions.map { it.habitId }.toSet(),
            habits = summary.habits,
            onMealClick = { viewingMeal = it }
        )

        Spacer(Modifier.height(80.dp))
    }

    viewingMeal?.let { m ->
        PhotoViewer(meal = m, onDismiss = { viewingMeal = null })
    }
}

@Composable
private fun DayCell(
    date: String,
    hasActivity: Boolean,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val cal = DateUtils.calendarFromIso(date)
    val day = cal.get(Calendar.DAY_OF_MONTH)
    val today = DateUtils.isToday(date)
    val future = DateUtils.isFuture(date)

    Box(
        modifier = Modifier
            .padding(2.dp)
            .height(44.dp)
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .background(
                when {
                    isSelected -> Color(0xFF1D9E75)
                    today -> Color(0xFFEAF3DE)
                    else -> Color.Transparent
                }
            )
            .clickable(enabled = !future) { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                "$day",
                color = when {
                    isSelected -> Color.White
                    future -> Color.LightGray
                    else -> Color.DarkGray
                },
                fontWeight = if (today) FontWeight.Bold else FontWeight.Normal
            )
            if (hasActivity && !isSelected) {
                Box(
                    modifier = Modifier
                        .padding(top = 2.dp)
                        .size(4.dp)
                        .clip(CircleShape)
                        .background(Color(0xFF1D9E75))
                )
            }
        }
    }
}

@Composable
private fun DaySummaryView(
    date: String,
    meals: List<Meal>,
    completedHabitIds: Set<Long>,
    habits: List<com.healthtracker.app.data.model.Habit>,
    onMealClick: (Meal) -> Unit
) {
    Column {
        Text(
            DateUtils.displayDayShort(date),
            fontWeight = FontWeight.SemiBold
        )
        Spacer(Modifier.height(8.dp))

        if (meals.isEmpty() && completedHabitIds.isEmpty()) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Text(
                    "Sin registros para este día.",
                    modifier = Modifier.padding(16.dp),
                    color = Color.Gray
                )
            }
            return
        }

        // Meals grid
        val mealsByType = meals.associateBy { it.type }
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            MealType.values().forEach { type ->
                val m = mealsByType[type]
                Box(modifier = Modifier.weight(1f)) {
                    Card(
                        modifier = Modifier.aspectRatio(1f),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        shape = RoundedCornerShape(14.dp)
                    ) {
                        Box(modifier = Modifier.fillMaxSize().padding(6.dp)) {
                            if (m?.photoPath != null) {
                                AsyncImage(
                                    model = File(m.photoPath),
                                    contentDescription = type.displayName,
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .clip(RoundedCornerShape(10.dp))
                                        .clickable { onMealClick(m) }
                                )
                            } else {
                                Box(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .clip(RoundedCornerShape(10.dp))
                                        .background(Color(0xFFF3F1EC)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(type.emoji, fontSize = 24.sp)
                                }
                            }
                        }
                    }
                }
            }
        }

        Spacer(Modifier.height(12.dp))

        // Habits list (only completed)
        if (habits.isNotEmpty()) {
            Text("Hábitos del día", fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
            Spacer(Modifier.height(6.dp))
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                habits.forEach { h ->
                    val done = completedHabitIds.contains(h.id)
                    val colors = ColorMap.colors(h.colorKey)
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = if (done) colors.bg else Color(0xFFF3F1EC)
                        ),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(32.dp)
                                    .clip(CircleShape)
                                    .background(if (done) Color.White else Color.LightGray),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = IconMap.icon(h.iconKey),
                                    contentDescription = h.title,
                                    tint = if (done) colors.fg else Color.DarkGray,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                            Spacer(Modifier.width(10.dp))
                            Text(
                                h.title,
                                color = if (done) colors.fg else Color.Gray,
                                fontWeight = if (done) FontWeight.SemiBold else FontWeight.Normal
                            )
                            Spacer(Modifier.weight(1f))
                            Text(
                                if (done) "✓" else "—",
                                color = if (done) colors.fg else Color.Gray,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }
    }
}
