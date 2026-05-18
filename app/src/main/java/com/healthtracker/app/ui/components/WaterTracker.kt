package com.healthtracker.app.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocalDrink
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
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
    onGlassClick: (Int) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    Icons.Filled.LocalDrink,
                    contentDescription = null,
                    tint = Color(0xFF185FA5)
                )
                Spacer(Modifier.width(8.dp))
                Text("Agua", fontWeight = FontWeight.SemiBold, fontSize = 16.sp)
                Spacer(Modifier.weight(1f))
                Text(
                    "$glasses / $goal",
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
            }
            Spacer(Modifier.height(12.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                for (i in 1..goal) {
                    val filled = i <= glasses
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(36.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(
                                if (filled) Color(0xFF185FA5) else Color(0xFFE6F1FB)
                            )
                            .clickable {
                                // Click on N-th glass → set to N. If clicking current, decrement.
                                val newValue = if (glasses == i) i - 1 else i
                                onGlassClick(newValue)
                            }
                    )
                }
            }
        }
    }
}
