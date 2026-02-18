package com.example.petal.domain

import com.example.petal.domain.Mood
import java.time.Instant

data class Memory(
    val id: Int,                     // server ID
    val title: String,
    val note: String,
    val location: Location?,
    val memoryDateTime: Instant?,
    val audioUrl: String?,
    val musicUrl: String?,
    val tags: List<String>,
    val mood: Mood?,
    val isFavorite: Boolean,
    val isDeleted: Boolean,
    val revision: Int,
    val createdAt: Instant,
    val updatedAt: Instant,
    val images: List<MemoryImage>
)