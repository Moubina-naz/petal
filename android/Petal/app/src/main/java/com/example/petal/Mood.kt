package com.example.petal

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.ui.graphics.vector.ImageVector

enum class Mood(
    val value: Int?,
    val label: String,
    val icon: ImageVector,
) {
    CALM(
        value = 1,
        label = "Calm",
        icon = Icons.Default.Person,

        ),
    HAPPY(
        value = 2,
        label = "Happy",
        icon = Icons.Default.Person,
    ),
    SAD(
        value = 3,
        label = "Sad",
        icon = Icons.Default.Person,
    ),
    ANXIOUS(
        value = 4,
        label = "Anxious",
        icon = Icons.Default.Person,
    ),
    EXCITED(
        value = 5,
        label = "Excited",
        icon = Icons.Default.Person,
    ),
    REFLECTIVE(
        value = 6,
        label = "Reflective",
        icon = Icons.Default.Person,
    ),
    GRATEFUL(
        value = 7,
        label = "Grateful",
        icon = Icons.Default.Person,
    ),
    STRESSED(
        value = 8,
        label = "Angry",
        icon = Icons.Default.Person,
    ),
    LONELY(
        value = 9,
        label = "Lonely",
        icon = Icons.Default.Person,
    ),
    CONTENT(
        value = 10,
        label = "Content",
        icon = Icons.Default.Person,
    );


    companion object {
        fun from(value: Int?): Mood? =
            values().find { it.value == value }
    }
}