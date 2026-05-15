package com.healthtracker.app.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.healthtracker.app.data.model.Habit
import com.healthtracker.app.util.ColorMap
import com.healthtracker.app.util.IconMap

@Composable
fun HabitRow(
    habit: Habit,
    done: Boolean,
    onToggle: () -> Unit,
    onDelete: () -> Unit
) {
    val colors = ColorMap.colors(habit.colorKey)

    Row(
        Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(Color.White)
            .border(0.5.dp, Color(0xFFEDEBE3), RoundedCornerShape(12.dp))
            .clickable(onClick = onToggle)
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            Modifier
                .size(44.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(colors.background),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                IconMap.icon(habit.iconKey),
                contentDescription = habit.title,
                tint = colors.foreground,
                modifier = Modifier.size(24.dp)
            )
        }
        Spacer(Modifier.width(12.dp))
        Column(Modifier.weight(1f)) {
            Text(habit.title, fontSize = 14.sp, fontWeight = FontWeight.Medium)
            if (habit.subtitle.isNotBlank()) {
                Text(habit.subtitle, fontSize = 12.sp, color = Color(0xFF888780))
            }
        }

        // Botón borrar solo para hábitos no-core
        if (!habit.isCore) {
            IconButton(onClick = onDelete, modifier = Modifier.size(28.dp)) {
                Icon(Icons.Filled.Delete, "Borrar",
                    tint = Color(0xFFB4B2A9), modifier = Modifier.size(18.dp))
            }
            Spacer(Modifier.width(4.dp))
        }

        // Checkbox circular
        Box(
            Modifier
                .size(28.dp)
                .clip(CircleShape)
                .background(if (done) Color(0xFF1D9E75) else Color.Transparent)
                .border(
                    width = if (done) 0.dp else 1.5.dp,
                    color = Color(0xFFB4B2A9),
                    shape = CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            if (done) {
                Icon(Icons.Filled.Check, null, tint = Color.White,
                    modifier = Modifier.size(16.dp))
            }
        }
    }
}
