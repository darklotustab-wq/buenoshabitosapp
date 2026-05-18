package com.healthtracker.app.data.model

enum class MealType(val displayName: String, val emoji: String) {
    BREAKFAST("Desayuno", "☕"),
    LUNCH("Almuerzo", "🥗"),
    SNACK("Merienda", "🍎"),
    DINNER("Cena", "🍽️")
}
