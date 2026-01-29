package com.example.petal.ui.editMemory

data class EditableImage(
    val localUri: String,          // content:// or file://
    val caption: String? = null,
    val order: Int
)