package com.example.petal.domain

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Air
import androidx.compose.material.icons.filled.AutoStories
import androidx.compose.material.icons.filled.Celebration
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PersonOutline
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.SentimentDissatisfied
import androidx.compose.material.icons.filled.SentimentSatisfied
import androidx.compose.material.icons.filled.SentimentSatisfiedAlt
import androidx.compose.material.icons.filled.SentimentVeryDissatisfied
import androidx.compose.material.icons.filled.Spa
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector

enum class Mood(
    val value: Int,
    val label: String,
    val icon: ImageVector,
    val color: Color
) {
    CALM(1, "Calm", Icons.Default.WbSunny,          Color(0xFFD6A54A)),  // warm gold — keep
    HAPPY(2, "Happy", Icons.Default.SentimentSatisfied, Color(0xFFE8729A)),  // warm pink
    SAD(3, "Sad", Icons.Default.WaterDrop,           Color(0xFF6B8FBF)),  // soft blue
    ANXIOUS(4, "Anxious", Icons.Default.Air,         Color(0xFFC97A4C)),  // terracotta — keep
    EXCITED(5, "Excited", Icons.Default.Celebration, Color(0xFFEC4F4F)),  // hot pink
    REFLECTIVE(6, "Reflective", Icons.Default.AutoStories, Color(0xFF8B7BB5)), // lavender
    GRATEFUL(7, "Grateful", Icons.Default.WbSunny,  Color(0xFFC9A44F)),  // golden — keep
    ANGRY(8, "Angry", Icons.Default.LocalFireDepartment, Color(0xFFA63C3C)), // deep red — keep
    LONELY(9, "Lonely", Icons.Default.PersonOutline, Color(0xFF7B8FC4)),  // periwinkle
    CONTENT(10, "Content", Icons.Default.Spa,        Color(0xFF7DB58A)); // sage green

    companion object {
        fun from(value: Int?): Mood? =
            values().find { it.value == value }
    }
}