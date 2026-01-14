package com.example.petal

import android.os.Build
import java.time.Instant


data class Memory(
    val id: Int,                     // server ID
    val title: String,
    val note: String,
    val location: Location?,
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





