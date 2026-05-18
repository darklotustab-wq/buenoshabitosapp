package com.healthtracker.app.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CameraAlt
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.healthtracker.app.data.model.Meal
import com.healthtracker.app.data.model.MealType
import java.io.File

@Composable
fun MealCard(
    type: MealType,
    meal: Meal?,
    onAddPhotoClick: () -> Unit,
    onViewPhotoClick: (Meal) -> Unit
) {
    val photoPath = meal?.photoPath
    Card(
        modifier = Modifier.fillMaxWidth().aspectRatio(1f),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(modifier = Modifier.fillMaxSize().padding(12.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(type.emoji, fontSize = 18.sp)
                Spacer(Modifier.width(6.dp))
                Text(
                    type.displayName,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 14.sp
                )
            }
            Spacer(Modifier.height(8.dp))
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(14.dp))
                    .background(Color(0xFFF3F1EC))
            ) {
                if (photoPath != null) {
                    AsyncImage(
                        model = File(photoPath),
                        contentDescription = type.displayName,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxSize()
                            .clickable { onViewPhotoClick(meal) }
                    )
                    // Botón flotante para reemplazar foto
                    Box(
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .padding(6.dp)
                            .size(32.dp)
                            .clip(CircleShape)
                            .background(Color.White.copy(alpha = 0.85f))
                            .clickable { onAddPhotoClick() },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Filled.CameraAlt,
                            contentDescription = "Reemplazar",
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                } else {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .clickable { onAddPhotoClick() },
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(
                                imageVector = Icons.Filled.Add,
                                contentDescription = "Agregar",
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(36.dp)
                            )
                            Spacer(Modifier.height(4.dp))
                            Text("Tocar para foto", fontSize = 12.sp, color = Color.Gray)
                        }
                    }
                }
            }
        }
    }
}
