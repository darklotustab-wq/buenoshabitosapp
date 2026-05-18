package com.healthtracker.app.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.healthtracker.app.util.ColorMap
import com.healthtracker.app.util.IconMap

@Composable
fun AddHabitDialog(
    onDismiss: () -> Unit,
    onConfirm: (title: String, iconKey: String, colorKey: String) -> Unit
) {
    var title by remember { mutableStateOf("") }
    var icon by remember { mutableStateOf("heart") }
    var color by remember { mutableStateOf("blue") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Nuevo hábito") },
        text = {
            Column {
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Título") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(Modifier.height(12.dp))
                Text("Ícono")
                Spacer(Modifier.height(8.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    IconMap.allKeys.take(5).forEach { key ->
                        IconChip(key, key == icon) { icon = key }
                    }
                }
                Row(modifier = Modifier.padding(top = 8.dp), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    IconMap.allKeys.drop(5).forEach { key ->
                        IconChip(key, key == icon) { icon = key }
                    }
                }
                Spacer(Modifier.height(12.dp))
                Text("Color")
                Spacer(Modifier.height(8.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    ColorMap.allKeys.take(4).forEach { key ->
                        ColorChip(key, key == color) { color = key }
                    }
                }
                Row(modifier = Modifier.padding(top = 8.dp), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    ColorMap.allKeys.drop(4).forEach { key ->
                        ColorChip(key, key == color) { color = key }
                    }
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    if (title.isNotBlank()) onConfirm(title.trim(), icon, color)
                },
                enabled = title.isNotBlank()
            ) { Text("Agregar") }
        },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Cancelar") } }
    )
}

@Composable
fun IconChip(key: String, selected: Boolean, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .size(40.dp)
            .clip(CircleShape)
            .background(if (selected) Color(0xFF1D9E75) else Color(0xFFE4E7E2))
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = IconMap.icon(key),
            contentDescription = key,
            tint = if (selected) Color.White else Color.DarkGray
        )
    }
}

@Composable
fun ColorChip(key: String, selected: Boolean, onClick: () -> Unit) {
    val c = ColorMap.colors(key)
    Box(
        modifier = Modifier
            .size(40.dp)
            .clip(CircleShape)
            .background(c.fg)
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        if (selected) {
            Box(
                modifier = Modifier
                    .size(14.dp)
                    .clip(CircleShape)
                    .background(Color.White)
            )
        }
    }
}
