package com.healthtracker.app.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun WaterTracker(
    glasses: Int,
    goal: Int,
    onAdd: () -> Unit,
    onRemove: () -> Unit
) {
    Row(
        Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(Color.White)
            .border(0.5.dp, Color(0xFFEDEBE3), RoundedCornerShape(12.dp))
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            Modifier
                .size(40.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(Color(0xFFE6F1FB)),
            contentAlignment = Alignment.Center
        ) {
            Icon(Icons.Filled.WaterDrop, null, tint = Color(0xFF185FA5),
                modifier = Modifier.size(22.dp))
        }
        Spacer(Modifier.width(12.dp))
        Column(Modifier.weight(1f)) {
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text("Agua", fontSize = 13.sp, fontWeight = FontWeight.Medium)
                Text("$glasses / $goal vasos", fontSize = 12.sp, color = Color(0xFF5F5E5A))
            }
            Spacer(Modifier.height(6.dp))
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(3.dp)) {
                repeat(goal) { i ->
                    Box(
                        Modifier
                            .weight(1f)
                            .height(6.dp)
                            .clip(RoundedCornerShape(3.dp))
                            .background(if (i < glasses) Color(0xFF378ADD) else Color(0xFFEDEBE3))
                    )
                }
            }
        }
        Spacer(Modifier.width(8.dp))
        IconButton(onClick = onRemove, modifier = Modifier.size(32.dp), enabled = glasses > 0) {
            Icon(Icons.Filled.Remove, "Quitar vaso", tint = Color(0xFF5F5E5A))
        }
        IconButton(onClick = onAdd, modifier = Modifier.size(32.dp), enabled = glasses < goal) {
            Icon(Icons.Filled.Add, "Agregar vaso", tint = Color(0xFF185FA5))
        }
    }
}
