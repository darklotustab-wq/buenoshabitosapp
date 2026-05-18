package com.healthtracker.app.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
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
    completed: Boolean,
    onToggle: (Boolean) -> Unit
) {
    val colors = ColorMap.colors(habit.colorKey)
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onToggle(!completed) },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = colors.bg)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(Color.White),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = IconMap.icon(habit.iconKey),
                    contentDescription = habit.title,
                    tint = colors.fg
                )
            }
            Spacer(Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(habit.title, fontWeight = FontWeight.SemiBold, color = colors.fg)
                habit.subtitle?.let {
                    Text(it, fontSize = 12.sp, color = colors.fg.copy(alpha = 0.7f))
                }
            }
            Box(
                modifier = Modifier
                    .size(28.dp)
                    .clip(CircleShape)
                    .background(if (completed) colors.fg else Color.White),
                contentAlignment = Alignment.Center
            ) {
                if (completed) {
                    Icon(
                        imageVector = Icons.Filled.Check,
                        contentDescription = "Completado",
                        tint = Color.White,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }
    }
}
