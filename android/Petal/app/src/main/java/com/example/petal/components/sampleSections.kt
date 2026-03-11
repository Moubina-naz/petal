package com.example.petal.components


import com.example.petal.domain.Memory
import com.example.petal.domain.MemoryImage
import com.example.petal.domain.Mood
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId

data class MemorySection(
    val title: String, // "TODAY", "YESTERDAY", "LAST WEEK"
    val memories: List<Memory>
)

fun groupMemories(memories: List<Memory>): List<MemorySection> {
    val today = LocalDate.now()
    val yesterday = today.minusDays(1)
    val weekAgo = today.minusDays(7)

    val todayList = mutableListOf<Memory>()
    val yesterdayList = mutableListOf<Memory>()
    val lastWeekList = mutableListOf<Memory>()
    val olderList = mutableListOf<Memory>()

    for (memory in memories) {
        val instant = memory.memoryDateTime ?: memory.createdAt
        val memoryDate = instant
            .atZone(ZoneId.systemDefault())
            .toLocalDate()

        when {
            !memoryDate.isBefore(today) -> todayList.add(memory)  // today or future
            memoryDate == yesterday -> yesterdayList.add(memory)
            memoryDate.isAfter(weekAgo) -> lastWeekList.add(memory)
            else -> olderList.add(memory)
        }
    }

    return buildList {
        if (todayList.isNotEmpty()) add(MemorySection("TODAY", todayList))
        if (yesterdayList.isNotEmpty()) add(MemorySection("YESTERDAY", yesterdayList))
        if (lastWeekList.isNotEmpty()) add(MemorySection("EARLIER THIS WEEK", lastWeekList))
        if (olderList.isNotEmpty()) add(MemorySection("OLDER", olderList))
    }
}



