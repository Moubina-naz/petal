package com.example.petal.ui.calendarScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.petal.NavigationEvent
import com.example.petal.components.MemoryCard
import com.example.petal.domain.Memory
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.ZoneId

@Composable
fun CalendarScreen(
    viewModel: CalendarViewModel,
    onNavigationEvent: (NavigationEvent) -> Unit = {}
) {
    val currentDate by viewModel.currentDate.collectAsState()
    val memoriesByDay by viewModel.memoriesByDay.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val selectedDay by viewModel.selectedDay.collectAsState()

    val selectedMemories = selectedDay?.let { memoriesByDay[it] } ?: emptyList()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFDF8F4))
            .statusBarsPadding()
    ) {
        // Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Calendar",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                fontSize = 30.sp,
                color = Color(0xFF3E2F26),
                modifier = Modifier.weight(1f)
            )
        }

        // Month navigation
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            IconButton(onClick = { viewModel.goToPreviousMonth() }) {
                Icon(
                    Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Previous month",
                    tint = Color(0xFF615A57)
                )
            }

            Text(
                text = currentDate.format(DateTimeFormatter.ofPattern("MMMM yyyy")),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF3E2F26)
            )

            IconButton(onClick = { viewModel.goToNextMonth() }) {
                Icon(
                    Icons.AutoMirrored.Filled.ArrowForward,
                    contentDescription = "Next month",
                    tint = Color(0xFF615A57)
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Day of week headers
        Row(modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp)) {
            listOf("S", "M", "T", "W", "T", "F", "S").forEach { day ->
                Text(
                    text = day,
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Center,
                    fontSize = 14.sp,
                    color = Color(0xFF7F4032),
                    fontWeight = FontWeight.Medium
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        if (isLoading) {
            Box(modifier = Modifier.fillMaxWidth().height(200.dp), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = Color(0xFF9E6F73))
            }
        } else {
            // Calendar grid
            CalendarGrid(
                currentDate = currentDate,
                memoriesByDay = memoriesByDay,
                selectedDay = selectedDay,
                onDayClick = { day -> viewModel.selectDay(day) }
            )
        }

        // Selected day memories
        if (selectedDay != null) {
            Spacer(modifier = Modifier.height(16.dp))

            val monthName = currentDate.format(DateTimeFormatter.ofPattern("MMM"))
            Text(
                text = "Memories for $monthName $selectedDay",
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                color = Color(0xFF313131)),
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            Spacer(modifier = Modifier.height(10.dp))

            if (selectedMemories.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxWidth().padding(32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text("No memories on this day", color = Color(0xFF9C8F86))
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxWidth(),
                    contentPadding = PaddingValues(horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(selectedMemories) { memory ->
                        MemoryCard(
                            memory = memory,
                            onMemoryClick = {
                                onNavigationEvent(
                                    NavigationEvent.OpenMemoryDetail(memory.id)
                                )
                            }
                        )
                    }
                    item { Spacer(modifier = Modifier.height(16.dp)) }
                }
            }
        }
    }
}
@Composable
fun CalendarGrid(
    currentDate: LocalDate,
    memoriesByDay: Map<Int, List<Memory>>,
    selectedDay: Int?,
    onDayClick: (Int) -> Unit
) {
    val firstDayOfMonth = currentDate.withDayOfMonth(1)
    val firstDayOfWeek = firstDayOfMonth.dayOfWeek.value % 7
    val daysInMonth = currentDate.month.length(currentDate.isLeapYear)
    val today = LocalDate.now()

    val cells = mutableListOf<Int?>()
    repeat(firstDayOfWeek) { cells.add(null) }
    for (day in 1..daysInMonth) cells.add(day)
    while (cells.size % 7 != 0) cells.add(null)

    Column(modifier = Modifier.padding(horizontal = 8.dp)) {
        cells.chunked(7).forEach { week ->
            Row(modifier = Modifier.fillMaxWidth()) {
                week.forEach { day ->
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .aspectRatio(1f)
                            .padding(2.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        if (day != null) {
                            val hasMemories = memoriesByDay.containsKey(day)
                            val isSelected = day == selectedDay
                            val isToday = currentDate.year == today.year &&
                                    currentDate.month == today.month &&
                                    day == today.dayOfMonth
                            val firstImage = memoriesByDay[day]?.firstOrNull()?.images?.firstOrNull()?.imageUrl

                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(2.dp)
                                    .clip(RoundedCornerShape(8.dp))
                                    .then(
                                        if (isSelected)
                                            Modifier.background(Color(0xFFa0bbb3))
                                        else
                                            Modifier.background(Color.Transparent)
                                    )
                                    .clickable { onDayClick(day) },
                                contentAlignment = Alignment.Center
                            ) {

                                if (hasMemories && firstImage != null && !isSelected) {
                                    AsyncImage(
                                        model = firstImage,
                                        contentDescription = null,
                                        contentScale = ContentScale.Crop,
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .clip(RoundedCornerShape(8.dp))
                                    )
                                    Box(
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .background(Color.Black.copy(alpha = 0.3f), RoundedCornerShape(8.dp))
                                    )
                                }

                                else if (hasMemories && !isSelected) {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .background(Color(0xFFe5a79a), RoundedCornerShape(8.dp))
                                    )
                                }

                                Text(
                                    text = day.toString(),
                                    fontSize = 13.sp,
                                    fontWeight = if (isToday || isSelected) FontWeight.Bold else FontWeight.Normal,
                                    color = when {
                                        isSelected -> Color.White
                                        hasMemories && firstImage != null -> Color.White
                                        isToday -> Color(0xFFC77B57)
                                        else -> Color(0xFF3E2F26)
                                    }
                                )

                                if (isToday && !isSelected) {
                                    Box(
                                        modifier = Modifier
                                            .align(Alignment.BottomCenter)
                                            .padding(bottom = 3.dp)
                                            .size(4.dp)
                                            .background(Color(0xFFC77B57), CircleShape)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun MemoryDayCard(
    memory: Memory,
    onClick: () -> Unit
) {
    val timeText = memory.memoryDateTime
        ?.atZone(ZoneId.systemDefault())
        ?.format(DateTimeFormatter.ofPattern("h:mm a"))
        ?: ""

    val locationText = memory.location?.name ?: ""

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Thumbnail
            val firstImage = memory.images.firstOrNull()?.imageUrl
            if (firstImage != null) {
                AsyncImage(
                    model = firstImage,
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(56.dp)
                        .clip(RoundedCornerShape(12.dp))
                )
            } else {
                Box(
                    modifier = Modifier
                        .size(56.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(0xFFEDE6DE)),
                    contentAlignment = Alignment.Center
                ) {
                    Text("📝", fontSize = 20.sp)
                }
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = memory.title,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF3E2F26)
                )
                Spacer(modifier = Modifier.height(4.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    if (timeText.isNotBlank()) {
                        Text("🕐 $timeText", fontSize = 12.sp, color = Color(0xFF9C8F86))
                    }
                    if (timeText.isNotBlank() && locationText.isNotBlank()) {
                        Text(" • ", fontSize = 12.sp, color = Color(0xFF9C8F86))
                    }
                    if (locationText.isNotBlank()) {
                        Text("📍 $locationText", fontSize = 12.sp, color = Color(0xFF9C8F86))
                    }
                }
            }

            Icon(
                Icons.AutoMirrored.Filled.ArrowForward,
                contentDescription = null,
                tint = Color(0xFFD6CCC2),
                modifier = Modifier.size(16.dp)
            )
        }
    }
}