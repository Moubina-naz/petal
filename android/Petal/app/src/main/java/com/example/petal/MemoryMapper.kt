package com.example.petal

import com.example.petal.domain.Location
import com.example.petal.domain.Memory
import com.example.petal.domain.MemoryImage
import com.example.petal.domain.Mood
import com.example.petal.dto.MemoryDto
import com.example.petal.dto.MemoryImageDto
import java.time.Instant
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter

object MemoryMapper {

    private val formatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME

    private fun mapImage(dto: MemoryImageDto): MemoryImage {
        return MemoryImage(
            id = dto.id,
            imageUrl = dto.imageUrl
                ?: throw IllegalStateException("image_url missing from API"),
            caption = dto.caption,
            order = dto.order,
            createdAt = Instant.parse(dto.createdAt)
        )

    }

    fun map(dto: MemoryDto): Memory {
        return Memory(
            id = dto.id,
            title = dto.title,
            note = dto.note,
            location = if (dto.latitude != null && dto.longitude != null) {
                Location(dto.latitude, dto.longitude, name = dto.locationName ?: "Unknown location")
            } else null,
            audioUrl = dto.audio?.takeIf { it.isNotBlank() },
            musicUrl = dto.musicUrl?.takeIf { it.isNotBlank() },
            tags = dto.tags,
            mood = Mood.from(dto.mood),
            isFavorite = dto.isFavorite,
            isDeleted = dto.isDeleted,
            revision = dto.revision,
            createdAt = OffsetDateTime.parse(dto.createdAt).toInstant(),
            updatedAt = OffsetDateTime.parse(dto.updatedAt).toInstant(),
            images = dto.images.map(::mapImage),
            memoryDateTime =
                dto.memoryDateTime?.let { OffsetDateTime.parse(it).toInstant() }
        )
    }
}
