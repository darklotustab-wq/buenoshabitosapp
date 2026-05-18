package com.healthtracker.app.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.healthtracker.app.data.model.Habit
import com.healthtracker.app.data.model.HabitCompletion
import com.healthtracker.app.data.repository.HealthRepository
import com.healthtracker.app.util.DateUtils
import com.healthtracker.app.util.HabitStats
import com.healthtracker.app.util.StatsCalculator
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class DailyCompletionRate(val date: String, val rate: Float)

data class ProgressUiState(
    val loading: Boolean = true,
    val habits: List<Habit> = emptyList(),
    val statsByHabit: Map<Long, HabitStats> = emptyMap(),
    val habitsRate30: Float = 0f,
    val mealsRate30: Float = 0f,
    val last7: List<DailyCompletionRate> = emptyList()
)

class ProgressViewModel(private val repo: HealthRepository) : ViewModel() {

    private val _state = MutableStateFlow(ProgressUiState())
    val state: StateFlow<ProgressUiState> = _state.asStateFlow()

    init { refresh() }

    fun refresh() = viewModelScope.launch {
        _state.value = _state.value.copy(loading = true)
        val habits = repo.getHabits()
        val last30 = DateUtils.lastNDaysIso(30)
        val start = last30.first()
        val end = last30.last()
        val completions = repo.completionsBetween(start, end)

        val stats = habits.associate { h ->
            h.id to StatsCalculator.statsFor(h, completions)
        }

        val habitsRate = StatsCalculator.habitsCompletionRate(habits, completions, 30)
        val datesWithMeals = repo.datesWithAnyActivity(start, end) // approx
        val mealsRate = StatsCalculator.mealCompletionRate(datesWithMeals, 30)

        val last7 = DateUtils.lastNDaysIso(7).map { date ->
            val n = completions.count { it.date == date }
            val possible = habits.size.coerceAtLeast(1)
            DailyCompletionRate(date, n / possible.toFloat())
        }

        _state.value = ProgressUiState(
            loading = false,
            habits = habits,
            statsByHabit = stats,
            habitsRate30 = habitsRate,
            mealsRate30 = mealsRate,
            last7 = last7
        )
    }

    class Factory(private val repo: HealthRepository) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T =
            ProgressViewModel(repo) as T
    }
}
