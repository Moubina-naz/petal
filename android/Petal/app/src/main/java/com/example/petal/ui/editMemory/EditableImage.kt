package com.example.petal.ui.editMemory

data class EditableImage(
    val localUri: String,
    val caption: String? = null,
    val order: Int
)