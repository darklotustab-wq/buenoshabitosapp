package com.healthtracker.app.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
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
    modifier: Modifier = Modifier
) {
    val (icon, bg, fg) = mealStyle(type)
    val hasPhoto = meal?.photoPath != null && File(meal.photoPath).exists()
    val isLogged = meal != null

    Column(
        modifier
            .clip(RoundedCornerShape(12.dp))
            .background(Color.White)
            .border(0.5.dp, Color(0xFFEDEBE3), RoundedCornerShape(12.dp))
            .clickable(onClick = onAddPhotoClick)
    ) {
        // Zona de la imagen / placeholder
        Box(
            Modifier
                .fillMaxWidth()
                .height(90.dp)
                .background(if (hasPhoto) Color.Transparent else bg),
            contentAlignment = Alignment.Center
        ) {
            if (hasPhoto) {
                AsyncImage(
                    model = meal!!.photoPath,
                    contentDescription = type.displayName,
                    modifier = Modifier.fillMaxSize()
                )
            } else if (isLogged) {
                Icon(icon, contentDescription = null, tint = fg,
                    modifier = Modifier.size(36.dp))
            } else {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(Icons.Filled.AddAPhoto, null,
                        tint = Color(0xFF5F5E5A), modifier = Modifier.size(28.dp))
                    Spacer(Modifier.height(4.dp))
                    Text("Subir foto", fontSize = 10.sp, color = Color(0xFF5F5E5A),
                        fontWeight = FontWeight.Medium)
                }
            }

            if (isLogged) {
                Box(
                    Modifier
                        .align(Alignment.TopEnd)
                        .padding(6.dp)
                        .size(20.dp)
                        .clip(CircleShape)
                        .background(Color(0xFF1D9E75)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Filled.Check, null,
                        tint = Color.White, modifier = Modifier.size(12.dp))
                }
            }
        }

        Column(Modifier.padding(horizontal = 10.dp, vertical = 8.dp)) {
            Text(type.displayName, fontSize = 13.sp, fontWeight = FontWeight.Medium)
            Text(
                meal?.description?.ifBlank { null } ?: if (isLogged) "Registrado" else "Pendiente",
                fontSize = 11.sp,
                color = Color(0xFF888780)
            )
        }
    }
}

private fun mealStyle(type: MealType): Triple<ImageVector, Color, Color> = when (type) {
    MealType.BREAKFAST -> Triple(Icons.Filled.LocalCafe,    Color(0xFFFAEEDA), Color(0xFF854F0B))
    MealType.LUNCH     -> Triple(Icons.Filled.RamenDining,  Color(0xFFEAF3DE), Color(0xFF3B6D11))
    MealType.SNACK     -> Triple(Icons.Filled.Cookie,       Color(0xFFFBEAF0), Color(0xFF993556))
    MealType.DINNER    -> Triple(Icons.Filled.DinnerDining, Color(0xFFE6F1FB), Color(0xFF185FA5))
}
