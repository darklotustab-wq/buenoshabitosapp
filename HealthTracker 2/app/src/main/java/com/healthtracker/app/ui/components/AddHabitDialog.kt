package com.healthtracker.app.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
    onConfirm: (title: String, subtitle: String, iconKey: String, colorKey: String) -> Unit
) {
    var title by remember { mutableStateOf("") }
    var subtitle by remember { mutableStateOf("") }
    var iconKey by remember { mutableStateOf("heart") }
    var colorKey by remember { mutableStateOf("pink") }

    val iconOptions = listOf("heart","book","water","sleep","run","music",
        "smile","meditation","pill")

    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(
                onClick = { onConfirm(title.trim(), subtitle.trim(), iconKey, colorKey) },
                enabled = title.isNotBlank()
            ) { Text("Agregar") }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancelar") }
        },
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
                Spacer(Modifier.height(8.dp))
                OutlinedTextField(
                    value = subtitle,
                    onValueChange = { subtitle = it },
                    label = { Text("Detalle (opcional)") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(Modifier.height(12.dp))
                Text("Ícono")
                Spacer(Modifier.height(6.dp))
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    iconOptions.take(5).forEach { k ->
                        IconPicker(k, iconKey == k) { iconKey = k }
                    }
                }
                Spacer(Modifier.height(6.dp))
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    iconOptions.drop(5).forEach { k ->
                        IconPicker(k, iconKey == k) { iconKey = k }
                    }
                }
                Spacer(Modifier.height(12.dp))
                Text("Color")
                Spacer(Modifier.height(6.dp))
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    ColorMap.available.forEach { c ->
                        ColorPicker(c, colorKey == c) { colorKey = c }
                    }
                }
            }
        }
    )
}

@Composable
private fun IconPicker(key: String, selected: Boolean, onClick: () -> Unit) {
    Box(
        Modifier
            .size(40.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(if (selected) Color(0xFFE6F1FB) else Color(0xFFF7F6F2))
            .border(
                if (selected) 1.5.dp else 0.5.dp,
                if (selected) Color(0xFF185FA5) else Color(0xFFEDEBE3),
                RoundedCornerShape(10.dp)
            )
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Icon(IconMap.icon(key), null, tint = Color(0xFF444441),
            modifier = Modifier.size(20.dp))
    }
}

@Composable
private fun ColorPicker(key: String, selected: Boolean, onClick: () -> Unit) {
    val colors = ColorMap.colors(key)
    Box(
        Modifier
            .size(28.dp)
            .clip(CircleShape)
            .background(colors.foreground)
            .border(
                if (selected) 2.dp else 0.dp,
                Color(0xFF1A1A1A),
                CircleShape
            )
            .clickable(onClick = onClick)
    )
}
