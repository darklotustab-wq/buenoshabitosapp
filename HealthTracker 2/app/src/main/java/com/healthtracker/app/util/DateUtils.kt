package com.healthtracker.app.util

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object DateUtils {
    private val isoFormat = SimpleDateFormat("yyyy-MM-dd", Locale.US)
    private val dayName   = SimpleDateFormat("EEEE", Locale("es", "AR"))
    private val dayLong   = SimpleDateFormat("d 'de' MMMM", Locale("es", "AR"))
    private val weekYear  = SimpleDateFormat("'Semana' w · yyyy", Locale("es", "AR"))

    fun todayIso(): String = isoFormat.format(Date())
    fun todayDayName(): String = dayName.format(Date()).replaceFirstChar { it.uppercase() }
    fun todayLong(): String = dayLong.format(Date())
    fun todayWeekYear(): String = weekYear.format(Date())
}
