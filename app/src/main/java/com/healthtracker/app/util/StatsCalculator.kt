package com.healthtracker.app.util

import com.healthtracker.app.data.model.Habit
import com.healthtracker.app.data.model.HabitCompletion
import java.util.Calendar

data class HabitStats(
    val currentStreak: Int,
    val bestStreak: Int,
    val completionsLast7: Int,
    val completionsLast30: Int,
    val percent30: Float
)

object StatsCalculator {

    fun statsFor(habit: Habit, completions: List<HabitCompletion>): HabitStats {
        val dates = completions.filter { it.habitId == habit.id }
            .map { it.date }
            .toSortedSet()

        // Current streak (consecutive days going backward from today)
        var current = 0
        val cal = Calendar.getInstance()
        while (true) {
            val iso = DateUtils.isoFromCalendar(cal)
            if (dates.contains(iso)) {
                current++
                cal.add(Calendar.DAY_OF_YEAR, -1)
            } else {
                // Allow today to be missing without breaking streak (yet)
                if (current == 0 && iso == DateUtils.todayIso()) {
                    cal.add(Calendar.DAY_OF_YEAR, -1)
                    continue
                }
                break
            }
        }

        // Best streak
        var best = 0
        var run = 0
        var prev: String? = null
        for (d in dates) {
            if (prev == null) {
                run = 1
            } else {
                val prevCal = DateUtils.calendarFromIso(prev)
                prevCal.add(Calendar.DAY_OF_YEAR, 1)
                val expected = DateUtils.isoFromCalendar(prevCal)
                run = if (expected == d) run + 1 else 1
            }
            best = maxOf(best, run)
            prev = d
        }

        val last7 = DateUtils.lastNDaysIso(7).count { dates.contains(it) }
        val last30Set = DateUtils.lastNDaysIso(30)
        val last30 = last30Set.count { dates.contains(it) }
        val pct = if (last30Set.isEmpty()) 0f else last30 / last30Set.size.toFloat()

        return HabitStats(
            currentStreak = current,
            bestStreak = best,
            completionsLast7 = last7,
            completionsLast30 = last30,
            percent30 = pct
        )
    }

    fun habitsCompletionRate(habits: List<Habit>, completions: List<HabitCompletion>, days: Int = 30): Float {
        if (habits.isEmpty()) return 0f
        val window = DateUtils.lastNDaysIso(days).toSet()
        val possible = habits.size * days
        if (possible == 0) return 0f
        val actual = completions.count { window.contains(it.date) }
        return actual / possible.toFloat()
    }

    fun mealCompletionRate(datesWithMeals: Set<String>, days: Int = 30): Float {
        val window = DateUtils.lastNDaysIso(days)
        if (window.isEmpty()) return 0f
        val hit = window.count { datesWithMeals.contains(it) }
        return hit / window.size.toFloat()
    }
}
