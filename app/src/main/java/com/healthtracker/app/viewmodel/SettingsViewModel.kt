package com.healthtracker.app.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.healthtracker.app.data.model.Habit
import com.healthtracker.app.data.repository.HealthRepository
import com.healthtracker.app.notifications.ReminderRegistry
import com.healthtracker.app.notifications.ReminderScheduler
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class SettingsUiState(
    val waterGoal: Int = 8,
    val remindersEnabled: Boolean = false,
    val reminderTimes: Map<String, String> = emptyMap()
)

class SettingsViewModel(
    private val repo: HealthRepository,
    private val appContext: Context
) : ViewModel() {

    private val _state = MutableStateFlow(SettingsUiState())
    val state: StateFlow<SettingsUiState> = _state.asStateFlow()

    val habits: StateFlow<List<Habit>> = repo.observeHabits()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    init {
        viewModelScope.launch {
            val wg = repo.getSetting("water_goal")?.toIntOrNull() ?: 8
            val remOn = repo.getSetting("reminders_enabled") == "true"
            val times = ReminderRegistry.all.associate { entry ->
                entry.key to (repo.getSetting(entry.key) ?: entry.defaultTime)
            }
            _state.value = SettingsUiState(
                waterGoal = wg,
                remindersEnabled = remOn,
                reminderTimes = times
            )
        }
    }

    fun setWaterGoal(value: Int) = viewModelScope.launch {
        val v = value.coerceIn(1, 20)
        _state.value = _state.value.copy(waterGoal = v)
        repo.setSetting("water_goal", v.toString())
    }

    fun setRemindersEnabled(enabled: Boolean) = viewModelScope.launch {
        _state.value = _state.value.copy(remindersEnabled = enabled)
        repo.setSetting("reminders_enabled", enabled.toString())
        if (enabled) {
            ReminderRegistry.all.forEach { entry ->
                val time = _state.value.reminderTimes[entry.key] ?: entry.defaultTime
                ReminderScheduler.schedule(appContext, entry, time)
            }
        } else {
            ReminderScheduler.cancelAll(appContext)
        }
    }

    fun setReminderTime(key: String, time: String) = viewModelScope.launch {
        val newTimes = _state.value.reminderTimes.toMutableMap().apply { put(key, time) }
        _state.value = _state.value.copy(reminderTimes = newTimes)
        repo.setSetting(key, time)
        if (_state.value.remindersEnabled) {
            val entry = ReminderRegistry.all.first { it.key == key }
            ReminderScheduler.cancel(appContext, entry)
            ReminderScheduler.schedule(appContext, entry, time)
        }
    }

    fun updateHabit(h: Habit) = viewModelScope.launch { repo.updateHabit(h) }
    fun deleteHabit(id: Long) = viewModelScope.launch { repo.deleteHabit(id) }

    fun wipeAll() = viewModelScope.launch {
        ReminderScheduler.cancelAll(appContext)
        repo.wipeAllData()
        _state.value = SettingsUiState()
    }

    class Factory(
        private val repo: HealthRepository,
        private val ctx: Context
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T =
            SettingsViewModel(repo, ctx) as T
    }
}
