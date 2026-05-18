package com.healthtracker.app.util

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

object DateUtils {
    private val iso = SimpleDateFormat("yyyy-MM-dd", Locale.US)
    private val dayName = SimpleDateFormat("EEEE", Locale("es"))
    private val dayShort = SimpleDateFormat("d MMM", Locale("es"))
    private val monthYear = SimpleDateFormat("MMMM yyyy", Locale("es"))

    fun todayIso(): String = iso.format(Date())

    fun isoFromCalendar(cal: Calendar): String = iso.format(cal.time)

    fun calendarFromIso(date: String): Calendar {
        val cal = Calendar.getInstance()
        cal.time = iso.parse(date) ?: Date()
        return cal
    }

    fun displayDayName(date: String = todayIso()): String =
        dayName.format(iso.parse(date) ?: Date()).replaceFirstChar { it.uppercase() }

    fun displayDayShort(date: String): String =
        dayShort.format(iso.parse(date) ?: Date())

    fun displayMonthYear(cal: Calendar): String =
        monthYear.format(cal.time).replaceFirstChar { it.uppercase() }

    fun displayBigDate(date: String = todayIso()): String {
        val d = iso.parse(date) ?: Date()
        val cal = Calendar.getInstance().apply { time = d }
        val day = cal.get(Calendar.DAY_OF_MONTH)
        val month = SimpleDateFormat("MMMM", Locale("es")).format(d)
        return "$day de $month"
    }

    /** Grilla de un mes: 6 filas x 7 cols. lunes=0..domingo=6. null = celda vacía. */
    fun monthGrid(cal: Calendar): List<String?> {
        val first = cal.clone() as Calendar
        first.set(Calendar.DAY_OF_MONTH, 1)
        // Convert Calendar.DAY_OF_WEEK (1=Sun..7=Sat) -> Lun=0..Dom=6
        val rawDow = first.get(Calendar.DAY_OF_WEEK)
        val offset = ((rawDow + 5) % 7)
        val daysInMonth = first.getActualMaximum(Calendar.DAY_OF_MONTH)
        val result = mutableListOf<String?>()
        repeat(offset) { result.add(null) }
        for (d in 1..daysInMonth) {
            first.set(Calendar.DAY_OF_MONTH, d)
            result.add(iso.format(first.time))
        }
        while (result.size < 42) result.add(null)
        return result
    }

    fun addMonths(cal: Calendar, delta: Int): Calendar {
        val c = cal.clone() as Calendar
        c.add(Calendar.MONTH, delta)
        return c
    }

    /** Lista de los últimos N días en ISO, del más antiguo al más reciente. */
    fun lastNDaysIso(n: Int): List<String> {
        val cal = Calendar.getInstance()
        val list = mutableListOf<String>()
        for (i in n - 1 downTo 0) {
            val c = cal.clone() as Calendar
            c.add(Calendar.DAY_OF_YEAR, -i)
            list.add(iso.format(c.time))
        }
        return list
    }

    /** Devuelve los 7 días de la semana actual (lunes a domingo). */
    fun thisWeekRange(): Pair<String, String> {
        val cal = Calendar.getInstance()
        val rawDow = cal.get(Calendar.DAY_OF_WEEK)
        val diffToMonday = ((rawDow + 5) % 7)
        val start = cal.clone() as Calendar
        start.add(Calendar.DAY_OF_YEAR, -diffToMonday)
        val end = start.clone() as Calendar
        end.add(Calendar.DAY_OF_YEAR, 6)
        return iso.format(start.time) to iso.format(end.time)
    }

    fun isToday(date: String): Boolean = date == todayIso()
    fun isFuture(date: String): Boolean = date > todayIso()
}
