package com.healthtracker.app.ui.settings

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.healthtracker.app.data.model.Habit
import com.healthtracker.app.ui.components.ColorChip
import com.healthtracker.app.ui.components.IconChip
import com.healthtracker.app.util.ColorMap
import com.healthtracker.app.util.IconMap

@Composable
fun EditHabitDialog(
    habit: Habit,
    onDismiss: () -> Unit,
    onSave: (Habit) -> Unit,
    onDelete: (() -> Unit)?
) {
    var title by remember { mutableStateOf(habit.title) }
    var subtitle by remember { mutableStateOf(habit.subtitle ?: "") }
    var icon by remember { mutableStateOf(habit.iconKey) }
    var color by remember { mutableStateOf(habit.colorKey) }
    var confirmDelete by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(if (habit.isCore) "Editar hábito base" else "Editar hábito") },
        text = {
            Column {
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Título") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(Modifier.height(8.dp))
                OutlinedTextField(
                    value = subtitle,
                    onValueChange = { subtitle = it },
                    label = { Text("Subtítulo (opcional)") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(Modifier.height(12.dp))
                Text("Ícono")
                Spacer(Modifier.height(6.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    IconMap.allKeys.take(5).forEach { k -> IconChip(k, k == icon) { icon = k } }
                }
                Row(modifier = Modifier.padding(top = 8.dp), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    IconMap.allKeys.drop(5).forEach { k -> IconChip(k, k == icon) { icon = k } }
                }
                Spacer(Modifier.height(12.dp))
                Text("Color")
                Spacer(Modifier.height(6.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    ColorMap.allKeys.take(4).forEach { k -> ColorChip(k, k == color) { color = k } }
                }
                Row(modifier = Modifier.padding(top = 8.dp), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    ColorMap.allKeys.drop(4).forEach { k -> ColorChip(k, k == color) { color = k } }
                }
                if (onDelete != null) {
                    Spacer(Modifier.height(12.dp))
                    TextButton(onClick = { confirmDelete = true }) {
                        Text("Eliminar hábito", color = MaterialTheme.colorScheme.error)
                    }
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    if (title.isNotBlank()) {
                        onSave(
                            habit.copy(
                                title = title.trim(),
                                subtitle = subtitle.trim().ifBlank { null },
                                iconKey = icon,
                                colorKey = color
                            )
                        )
                    }
                },
                enabled = title.isNotBlank()
            ) { Text("Guardar") }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancelar") }
        }
    )

    if (confirmDelete && onDelete != null) {
        AlertDialog(
            onDismissRequest = { confirmDelete = false },
            title = { Text("¿Eliminar hábito?") },
            text = { Text("Se borrará también el historial de cumplimiento de \"${habit.title}\".") },
            confirmButton = {
                TextButton(onClick = { confirmDelete = false; onDelete() }) {
                    Text("Eliminar", color = MaterialTheme.colorScheme.error)
                }
            },
            dismissButton = {
                TextButton(onClick = { confirmDelete = false }) { Text("Cancelar") }
            }
        )
    }
}
