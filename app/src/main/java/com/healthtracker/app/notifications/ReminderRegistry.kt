package com.healthtracker.app.notifications

/** Cada hábito base puede tener un recordatorio configurable. */
data class ReminderEntry(
    val id: Int,
    val key: String,           // settings key
    val title: String,
    val body: String,
    val defaultTime: String    // HH:mm
)

object ReminderRegistry {
    val all = listOf(
        ReminderEntry(101, "reminder_pill",       "Medicación",      "Es hora de tu medicación 💊",    "08:00"),
        ReminderEntry(102, "reminder_activity",   "Actividad física","Movete un rato 🏃",              "18:00"),
        ReminderEntry(103, "reminder_instrument", "Instrumento",     "Practicá tu instrumento 🎵",      "19:00"),
        ReminderEntry(104, "reminder_positive",   "Algo positivo",   "Anotá algo positivo del día 😊", "21:00"),
        ReminderEntry(105, "reminder_meditation", "Meditación",      "Momento de meditación 🧘",        "22:00")
    )
}
