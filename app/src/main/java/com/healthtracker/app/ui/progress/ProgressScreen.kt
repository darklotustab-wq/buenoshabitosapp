package com.healthtracker.app.ui.progress

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.healthtracker.app.data.model.Habit
import com.healthtracker.app.data.repository.HealthRepository
import com.healthtracker.app.util.ColorMap
import com.healthtracker.app.util.DateUtils
import com.healthtracker.app.util.HabitStats
import com.healthtracker.app.util.IconMap
import com.healthtracker.app.viewmodel.DailyCompletionRate
import com.healthtracker.app.viewmodel.ProgressViewModel
import java.util.Calendar

@Composable
fun ProgressScreen(repo: HealthRepository) {
    val vm: ProgressViewModel = viewModel(factory = ProgressViewModel.Factory(repo))
    val state by vm.state.collectAsState()

    // Refrescar cuando se entra a la pantalla
    LaunchedEffect(Unit) { vm.refresh() }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        Text(
            "Progreso",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )
        Spacer(Modifier.height(4.dp))
        Text("Últimos 30 días", color = Color.Gray, fontSize = 13.sp)
        Spacer(Modifier.height(16.dp))

        // KPIs
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            KpiCard(
                modifier = Modifier.weight(1f),
                title = "Hábitos",
                value = "${(state.habitsRate30 * 100).toInt()}%",
                color = Color(0xFF1D9E75)
            )
            KpiCard(
                modifier = Modifier.weight(1f),
                title = "Comidas",
                value = "${(state.mealsRate30 * 100).toInt()}%",
                color = Color(0xFFBA7517)
            )
        }

        Spacer(Modifier.height(20.dp))

        // Gráfico últimos 7 días
        Text("Últimos 7 días", fontWeight = FontWeight.SemiBold)
        Spacer(Modifier.height(8.dp))
        Last7BarChart(data = state.last7)

        Spacer(Modifier.height(20.dp))

        Text("Estadísticas por hábito", fontWeight = FontWeight.SemiBold)
        Spacer(Modifier.height(8.dp))

        if (state.habits.isEmpty()) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Text(
                    "Todavía no tenés hábitos.",
                    modifier = Modifier.padding(16.dp),
                    color = Color.Gray
                )
            }
        } else {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                state.habits.forEach { habit ->
                    val stats = state.statsByHabit[habit.id] ?: HabitStats(0, 0, 0, 0, 0f)
                    HabitStatRow(habit, stats)
                }
            }
        }

        Spacer(Modifier.height(80.dp))
    }
}

@Composable
private fun KpiCard(modifier: Modifier, title: String, value: String, color: Color) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(title, color = Color.Gray, fontSize = 13.sp)
            Spacer(Modifier.height(4.dp))
            Text(value, fontSize = 28.sp, fontWeight = FontWeight.Bold, color = color)
        }
    }
}

@Composable
private fun Last7BarChart(data: List<DailyCompletionRate>) {
    val labels = listOf("L", "M", "M", "J", "V", "S", "D")
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .height(140.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Bottom
        ) {
            data.forEachIndexed { idx, item ->
                val rate = item.rate.coerceIn(0f, 1f)
                val barColor = when {
                    rate >= 0.7f -> Color(0xFF3B6D11)
                    rate >= 0.4f -> Color(0xFFBA7517)
                    rate > 0f -> Color(0xFFA32D2D)
                    else -> Color(0xFFE4E7E2)
                }
                val cal = DateUtils.calendarFromIso(item.date)
                val dow = (cal.get(Calendar.DAY_OF_WEEK) + 5) % 7
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Bottom,
                    modifier = Modifier.weight(1f)
                ) {
                    Box(
                        modifier = Modifier
                            .width(20.dp)
                            .fillMaxHeight(rate.coerceAtLeast(0.04f))
                            .clip(RoundedCornerShape(topStart = 6.dp, topEnd = 6.dp))
                            .background(barColor)
                    )
                    Spacer(Modifier.height(6.dp))
                    Text(labels.getOrNull(dow) ?: "?", fontSize = 11.sp, color = Color.Gray)
                }
            }
        }
    }
}

@Composable
private fun HabitStatRow(habit: Habit, stats: HabitStats) {
    val colors = ColorMap.colors(habit.colorKey)
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(14.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(colors.bg),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = IconMap.icon(habit.iconKey),
                        contentDescription = habit.title,
                        tint = colors.fg,
                        modifier = Modifier.size(20.dp)
                    )
                }
                Spacer(Modifier.width(10.dp))
                Text(habit.title, fontWeight = FontWeight.SemiBold, modifier = Modifier.weight(1f))
                Text(
                    "${(stats.percent30 * 100).toInt()}%",
                    fontWeight = FontWeight.Bold,
                    color = colors.fg
                )
            }
            Spacer(Modifier.height(8.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(6.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .background(Color(0xFFEFEFEF))
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .fillMaxWidth(stats.percent30.coerceIn(0f, 1f))
                        .background(colors.fg)
                )
            }
            Spacer(Modifier.height(10.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                StatChip("🔥 Racha", "${stats.currentStreak}d")
                StatChip("Mejor", "${stats.bestStreak}d")
                StatChip("Semana", "${stats.completionsLast7}/7")
                StatChip("30d", "${stats.completionsLast30}")
            }
        }
    }
}

@Composable
private fun StatChip(label: String, value: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(value, fontWeight = FontWeight.Bold, fontSize = 14.sp)
        Text(label, fontSize = 10.sp, color = Color.Gray)
    }
}
