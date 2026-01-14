package com.example.petal

import java.time.Instant

data class MemoryImage(
    val id: Int,
    val imageUrl: String,
    val caption: String?,
    val order: Int,
    val createdAt: Instant
)