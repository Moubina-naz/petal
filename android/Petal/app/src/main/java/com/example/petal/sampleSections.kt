package com.example.petal


import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId

data class MemorySection(
    val title: String, // "TODAY", "YESTERDAY", "LAST WEEK"
    val memories: List<Memory>
)

fun groupMemories(memories: List<Memory>): List<MemorySection> {
    val today = LocalDate.now(ZoneId.systemDefault())
    val yesterday = today.minusDays(1)
    val weekAgo = today.minusDays(7)

    val todayList = mutableListOf<Memory>()
    val yesterdayList = mutableListOf<Memory>()
    val lastWeekList = mutableListOf<Memory>()

    for (memory in memories) {
        val memoryDate = Instant.ofEpochMilli(memory.createdAt.toEpochMilli())
            .atZone(ZoneId.systemDefault())
            .toLocalDate()

        when {
            memoryDate == today -> todayList.add(memory)
            memoryDate == yesterday -> yesterdayList.add(memory)
            memoryDate.isAfter(weekAgo) -> lastWeekList.add(memory)
        }
    }

    val sections = mutableListOf<MemorySection>()
    if (todayList.isNotEmpty()) sections.add(MemorySection("TODAY", todayList))
    if (yesterdayList.isNotEmpty()) sections.add(MemorySection("YESTERDAY", yesterdayList))
    if (lastWeekList.isNotEmpty()) sections.add(MemorySection("LAST WEEK", lastWeekList))

    return sections

}
val sampleMemories = listOf(
    Memory(
        serverId = 1,
        title = "Morning Quiet",
        note = "The light hitting the kitchen table was perfect today. Felt a sense of calm ☕",
        mood = Mood.GOOD,
        images = listOf(
            MemoryImage(
                id = 1,
                memoryId = 1,
                imageUrl = "https://images.unsplash.com/photo-1495474472287-4d71bcdd2085?ixlib=rb-4.0.3&w=800&auto=format&fit=crop",
                caption = "Coffee moment"
            )
        ),
        createdAt = Instant.now().minusSeconds(4 * 3600) // 4 hours ago → TODAY
    ),
    Memory(
        serverId = 2,
        title = "A heavy thought",
        note = "Sometimes I wonder if I'm moving fast enough. Everyone else seems to have it figured out, but I'm still just putting one foot in front of the other. Need to be patient with myself.",
        mood = Mood.NEUTRAL,
        images = emptyList(),
        createdAt = Instant.now().minusSeconds(28 * 3600) // yesterday evening → YESTERDAY
    ),
    Memory(
        serverId = 3,
        title = "Beach Walk",
        note = "Salt air cures everything.",
        mood = Mood.VERY_GOOD,
        images = listOf(
            MemoryImage(
                id = 3,
                memoryId = 3,
                imageUrl = "https://images.unsplash.com/photo-1507525428034-b723cf961d3e?ixlib=rb-4.0.3&w=1200&auto=format&fit=crop",
                caption = "Sunset walk"
            )
        ),
        createdAt = Instant.now().minusSeconds(2 * 24 * 3600) // 2 days ago
    ),
    Memory(
        serverId = 4,
        title = "Voice Note: Rain",
        note = "Just recorded the sound of rain on the window. So peaceful.",
        audioUrl = "fake_audio_url",
        createdAt = Instant.now().minusSeconds(5 * 24 * 3600) // 5 days ago → LAST WEEK
    ),
    Memory(
        serverId = 5,
        title = "Book Club",
        note = "Finished 'The Midnight Library'. Such a profound take on regret.",
        createdAt = Instant.now().minusSeconds(6 * 24 * 3600)
    )
)

val sampleSections = groupMemories(sampleMemories)