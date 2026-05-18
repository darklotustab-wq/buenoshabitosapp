package com.healthtracker.app.notifications

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import com.healthtracker.app.R

class ReminderReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val id = intent.getIntExtra("id", 0)
        val title = intent.getStringExtra("title") ?: "Recordatorio"
        val body = intent.getStringExtra("body") ?: ""

        val nm = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val channelId = "healthtracker_reminders"
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val ch = NotificationChannel(
                channelId,
                "Recordatorios HealthTracker",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            nm.createNotificationChannel(ch)
        }

        val notif = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle(title)
            .setContentText(body)
            .setAutoCancel(true)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .build()

        nm.notify(id, notif)
    }
}
