package com.healthtracker.app.notifications

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import java.util.Calendar

object ReminderScheduler {

    fun pendingIntentFor(context: Context, entry: ReminderEntry, flags: Int): PendingIntent {
        val intent = Intent(context, ReminderReceiver::class.java).apply {
            putExtra("id", entry.id)
            putExtra("title", entry.title)
            putExtra("body", entry.body)
        }
        return PendingIntent.getBroadcast(context, entry.id, intent, flags)
    }

    fun schedule(context: Context, entry: ReminderEntry, time: String) {
        val am = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val (h, m) = parseTime(time)
        val cal = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, h)
            set(Calendar.MINUTE, m)
            set(Calendar.SECOND, 0)
            if (timeInMillis <= System.currentTimeMillis()) {
                add(Calendar.DAY_OF_YEAR, 1)
            }
        }
        val pi = pendingIntentFor(context, entry,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)
        am.setRepeating(
            AlarmManager.RTC_WAKEUP,
            cal.timeInMillis,
            AlarmManager.INTERVAL_DAY,
            pi
        )
    }

    fun cancel(context: Context, entry: ReminderEntry) {
        val am = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val pi = pendingIntentFor(context, entry,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)
        am.cancel(pi)
    }

    fun cancelAll(context: Context) {
        ReminderRegistry.all.forEach { cancel(context, it) }
    }

    private fun parseTime(s: String): Pair<Int, Int> {
        val parts = s.split(":")
        val h = parts.getOrNull(0)?.toIntOrNull() ?: 8
        val m = parts.getOrNull(1)?.toIntOrNull() ?: 0
        return h to m
    }
}
