package com.healthtracker.app.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.healthtracker.app.data.model.*
import com.healthtracker.app.data.repository.HealthRepository
import com.healthtracker.app.util.DateUtils
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.Calendar

data class DaySummary(
    val date: String,
    val meals: List<Meal>,
    val completions: List<HabitCompletion>,
    val habits: List<Habit>
)

class HistoryViewModel(private val repo: HealthRepository) : ViewModel() {

    private val _month = MutableStateFlow(Calendar.getInstance())
    val month: StateFlow<Calendar> = _month

    private val _selectedDate = MutableStateFlow(DateUtils.todayIso())
    val selectedDate: StateFlow<String> = _selectedDate

    private val _activityDates = MutableStateFlow<Set<String>>(emptySet())
    val activityDates: StateFlow<Set<String>> = _activityDates

    val habits: StateFlow<List<Habit>> = repo.observeHabits()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    @OptIn(kotlinx.coroutines.ExperimentalCoroutinesApi::class)
    val daySummary: StateFlow<DaySummary> = combine(
        _selectedDate,
        habits
    ) { date, hs -> date to hs }
        .flatMapLatest { (date, hs) ->
            combine(
                repo.mealsForDate(date),
                repo.completionsForDate(date)
            ) { meals, comps ->
                DaySummary(date, meals, comps, hs)
            }
        }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            DaySummary(DateUtils.todayIso(), emptyList(), emptyList(), emptyList())
        )

    init {
        reloadActivityDates()
    }

    fun selectDate(date: String) { _selectedDate.value = date }

    fun changeMonth(delta: Int) {
        _month.value = DateUtils.addMonths(_month.value, delta)
        reloadActivityDates()
    }

    private fun reloadActivityDates() = viewModelScope.launch {
        val grid = DateUtils.monthGrid(_month.value).filterNotNull()
        if (grid.isEmpty()) return@launch
        val start = grid.first()
        val end = grid.last()
        _activityDates.value = repo.datesWithAnyActivity(start, end)
    }

    class Factory(private val repo: HealthRepository) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T =
            HistoryViewModel(repo) as T
    }
}
