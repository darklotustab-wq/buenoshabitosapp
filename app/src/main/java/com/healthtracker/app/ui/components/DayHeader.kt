package com.healthtracker.app.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LinearProgressIndicator
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
import com.healthtracker.app.util.DateUtils

@Composable
fun DayHeader(progress: Float) {
    Column(modifier = Modifier.fillMaxWidth().padding(horizontal = 4.dp, vertical = 8.dp)) {
        Text(
            text = DateUtils.displayDayName(),
            color = MaterialTheme.colorScheme.primary,
            fontWeight = FontWeight.SemiBold
        )
        Text(
            text = DateUtils.displayBigDate(),
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF1B1B1B)
        )
        Spacer(Modifier.height(12.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            LinearProgressIndicator(
                progress = progress.coerceIn(0f, 1f),
                modifier = Modifier
                    .weight(1f)
                    .height(8.dp)
                    .clip(RoundedCornerShape(8.dp)),
                color = MaterialTheme.colorScheme.primary,
                trackColor = Color(0xFFE4E7E2)
            )
            Spacer(Modifier.width(12.dp))
            Text(
                "${(progress * 100).toInt()}%",
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}
