package com.healthtracker.app.notifications

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.healthtracker.app.data.local.AppDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class BootReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action != Intent.ACTION_BOOT_COMPLETED) return
        val pendingResult = goAsync()
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val db = AppDatabase.get(context)
                val masterOn = db.settingsDao().get("reminders_enabled")?.value == "true"
                if (masterOn) {
                    ReminderRegistry.all.forEach { entry ->
                        val time = db.settingsDao().get(entry.key)?.value ?: entry.defaultTime
                        ReminderScheduler.schedule(context, entry, time)
                    }
                }
            } finally {
                pendingResult.finish()
            }
        }
    }
}
