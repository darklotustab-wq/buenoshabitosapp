package com.healthtracker.app.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.healthtracker.app.data.model.*
import com.healthtracker.app.data.repository.HealthRepository
import com.healthtracker.app.util.DateUtils
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

data class DashboardState(
    val date: String = DateUtils.todayIso(),
    val dayName: String = DateUtils.todayDayName(),
    val dayLong: String = DateUtils.todayLong(),
    val weekYear: String = DateUtils.todayWeekYear(),
    val meals: List<Meal> = emptyList(),
    val habits: List<Habit> = emptyList(),
    val completedHabitIds: Set<Long> = emptySet(),
    val waterGlasses: Int = 0,
    val waterGoal: Int = 8
) {
    val mealsByType: Map<MealType, Meal?> get() =
        MealType.values().associateWith { type -> meals.firstOrNull { it.type == type } }

    val mealsLogged: Int get() = meals.size
    val habitsDone: Int get() = completedHabitIds.size
    val habitsTotal: Int get() = habits.size

    /** progreso global: comidas + hábitos */
    val totalDone: Int get() = mealsLogged + habitsDone
    val totalGoal: Int get() = MealType.values().size + habitsTotal
    val progress: Float get() =
        if (totalGoal == 0) 0f else totalDone.toFloat() / totalGoal
}

class DashboardViewModel(private val repo: HealthRepository) : ViewModel() {

    private val today = DateUtils.todayIso()

    val state: StateFlow<DashboardState> = combine(
        repo.mealsForDate(today),
        repo.allHabits(),
        repo.completionsForDate(today),
        repo.waterForDate(today)
    ) { meals, habits, completions, water ->
        DashboardState(
            meals = meals,
            habits = habits,
            completedHabitIds = completions.map { it.habitId }.toSet(),
            waterGlasses = water?.glasses ?: 0,
            waterGoal = water?.goal ?: 8
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), DashboardState())

    fun toggleHabit(habit: Habit) {
        val done = state.value.completedHabitIds.contains(habit.id)
        viewModelScope.launch { repo.toggleHabit(habit.id, today, done) }
    }

    fun saveMealPhoto(type: MealType, photoPath: String, description: String = "") {
        viewModelScope.launch {
            val existing = repo.getMeal(today, type)
            repo.saveMeal(
                (existing ?: Meal(date = today, type = type)).copy(
                    photoPath = photoPath,
                    description = description.ifBlank { existing?.description ?: "" }
                )
            )
        }
    }

    fun addWater() {
        val s = state.value
        viewModelScope.launch {
            repo.setWater(today, s.waterGlasses + 1, s.waterGoal)
        }
    }

    fun removeWater() {
        val s = state.value
        viewModelScope.launch {
            repo.setWater(today, s.waterGlasses - 1, s.waterGoal)
        }
    }

    fun addCustomHabit(title: String, subtitle: String, iconKey: String, colorKey: String) {
        viewModelScope.launch {
            val orderIndex = (state.value.habits.maxOfOrNull { it.orderIndex } ?: 0) + 1
            repo.addHabit(
                Habit(
                    title = title, subtitle = subtitle,
                    iconKey = iconKey, colorKey = colorKey,
                    isCore = false, orderIndex = orderIndex
                )
            )
        }
    }

    fun deleteHabit(habit: Habit) {
        if (habit.isCore) return // los core no se borran
        viewModelScope.launch { repo.deleteHabit(habit) }
    }

    class Factory(private val repo: HealthRepository) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T =
            DashboardViewModel(repo) as T
    }
}
