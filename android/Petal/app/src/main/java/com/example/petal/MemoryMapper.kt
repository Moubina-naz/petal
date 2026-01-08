package com.example.petal

import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter

object MemoryMapper {

    private val formatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME
    private fun mapImage(dto: MemoryImageDto): MemoryImage {
        return MemoryImage(
            id = dto.id,
            memoryId = dto.memoryId,
            imageUrl = dto.image,
            caption = dto.caption,
            order = dto.order,
            createdAt = OffsetDateTime.parse(dto.createdAt, formatter).toInstant()
        )
    }

    fun map(memoryDto: MemoryDto,images: List<MemoryImageDto>): Memory {
        return Memory(
            serverId = memoryDto.id,
            title = memoryDto.title,
            note = memoryDto.note,
            location = if (memoryDto.latitude != null && memoryDto.longitude != null) {
                Location(memoryDto.latitude, memoryDto.longitude)
            } else null,
            audioUrl = memoryDto.audio?.takeIf { it.isNotEmpty() },
            musicUrl = memoryDto.musicUrl?.takeIf { it.isNotEmpty() },
            tags = memoryDto.tags,
            mood = Mood.from(memoryDto.mood),
            isFavorite = memoryDto.isFavorite,
            isDeleted = memoryDto.isDeleted,
            revision = memoryDto.revision,
            createdAt = OffsetDateTime.parse(memoryDto.createdAt, formatter).toInstant(),
            updatedAt = OffsetDateTime.parse(memoryDto.updatedAt, formatter).toInstant(),
        )
    }

}