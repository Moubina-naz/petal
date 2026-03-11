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
    CALM(1, "Calm", Icons.Default.WbSunny, Color(0xFFD6A54A)),
    HAPPY(2, "Happy", Icons.Default.SentimentSatisfied, Color(0xFFD86C5B)),
    SAD(3, "Sad", Icons.Default.WaterDrop, Color(0xFF5C8D82)),
    ANXIOUS(4, "Anxious", Icons.Default.Air, Color(0xFFC97A4C)),
    EXCITED(5, "Excited", Icons.Default.Celebration, Color(0xFFC75B39)),
    REFLECTIVE(6, "Reflective", Icons.Default.AutoStories, Color(0xFF6F8F7A)),
    GRATEFUL(7, "Grateful", Icons.Default.WbSunny, Color(0xFFC9A44F)),
    ANGRY(8, "Angry", Icons.Default.LocalFireDepartment, Color(0xFFA64B3C)),
    LONELY(9, "Lonely", Icons.Default.PersonOutline, Color(0xFF7C8A96)),
    CONTENT(10, "Content", Icons.Default.Spa, Color(0xFF8A9A5B));

    companion object {
        fun from(value: Int?): Mood? =
            values().find { it.value == value }
    }
}