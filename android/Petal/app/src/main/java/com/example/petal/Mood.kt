package com.example.petal

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.ui.graphics.vector.ImageVector

enum class Mood(
    val value: Int,
    val label: String,
    val icon: ImageVector
) {
    CALM(1, "Calm", Icons.Default.Person),
    HAPPY(2, "Happy", Icons.Default.Person),
    SAD(3, "Sad", Icons.Default.Person),
    ANXIOUS(4, "Anxious", Icons.Default.Person),
    EXCITED(5, "Excited", Icons.Default.Person),
    REFLECTIVE(6, "Reflective", Icons.Default.Person),
    GRATEFUL(7, "Grateful", Icons.Default.Person),
    ANGRY(8, "Angry", Icons.Default.Person),
    LONELY(9, "Lonely", Icons.Default.Person),
    CONTENT(10, "Content", Icons.Default.Person);

    companion object {
        fun from(value: Int?): Mood? =
            values().find { it.value == value }
    }
}
