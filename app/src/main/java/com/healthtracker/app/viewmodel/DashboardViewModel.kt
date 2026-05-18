package com.healthtracker.app.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.healthtracker.app.data.model.*
import com.healthtracker.app.data.repository.HealthRepository
import com.healthtracker.app.util.DateUtils
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class DashboardViewModel(private val repo: HealthRepository) : ViewModel() {

    private val today = MutableStateFlow(DateUtils.todayIso())

    val habits: StateFlow<List<Habit>> = repo.observeHabits()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    @OptIn(kotlinx.coroutines.ExperimentalCoroutinesApi::class)
    val todayMeals: StateFlow<List<Meal>> = today
        .flatMapLatest { repo.mealsForDate(it) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    @OptIn(kotlinx.coroutines.ExperimentalCoroutinesApi::class)
    val todayCompletions: StateFlow<List<HabitCompletion>> = today
        .flatMapLatest { repo.completionsForDate(it) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    @OptIn(kotlinx.coroutines.ExperimentalCoroutinesApi::class)
    val water: StateFlow<WaterIntake?> = today
        .flatMapLatest { repo.waterForDate(it) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    val waterGoal: StateFlow<Int> = flow {
        emit((repo.getSetting("water_goal")?.toIntOrNull() ?: 8))
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 8)

    fun setGlasses(glasses: Int) = viewModelScope.launch {
        repo.setGlasses(today.value, glasses)
    }

    fun saveMealPhoto(type: MealType, path: String) = viewModelScope.launch {
        repo.saveMealPhoto(today.value, type, path)
    }

    fun toggleHabit(habit: Habit, completed: Boolean) = viewModelScope.launch {
        repo.toggleCompletion(habit.id, today.value, completed)
    }

    fun addCustomHabit(title: String, iconKey: String, colorKey: String) =
        viewModelScope.launch {
            val all = repo.getHabits()
            repo.upsertHabit(
                Habit(
                    title = title,
                    iconKey = iconKey,
                    colorKey = colorKey,
                    isCore = false,
                    sortOrder = (all.maxOfOrNull { it.sortOrder } ?: 0) + 1
                )
            )
        }

    class Factory(private val repo: HealthRepository) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T =
            DashboardViewModel(repo) as T
    }
}
