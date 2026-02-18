package com.example.petal.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.petal.domain.Memory
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@Composable
fun MemoryCard(
    memory: Memory,
    onMemoryClick: () -> Unit = {},
    onFavoriteClick: () -> Unit = {},
) {
    val instant = memory.memoryDateTime ?: memory.createdAt

    val subtitle = buildString {
        append(
            instant.atZone(ZoneId.systemDefault())
                .format(DateTimeFormatter.ofPattern("MMM dd • hh:mm a"))
        )
        append(" · ")
        append(memory.location?.name ?: "Unknown location")
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onMemoryClick() }
            .background(
                color = Color(0xFFF6F2ED),
                shape = RoundedCornerShape(16.dp)
            )
            .padding(16.dp)
    ) {

        // ✅ Title
        Text(
            text = memory.title,
            style = MaterialTheme.typography.headlineMedium,
            color = Color(0xFF4E3A2F)
        )

        Spacer(modifier = Modifier.height(6.dp))

        // ✅ Subtitle (date + time + location)
        Text(
            text = subtitle,
            style = MaterialTheme.typography.labelMedium,
            color = Color(0xFF9C8F86)
        )

        Spacer(modifier = Modifier.height(8.dp))

        // ✅ Note preview
        Text(
            text = memory.note,
            style = MaterialTheme.typography.bodyMedium,
            color = Color(0xFF7A6A5E),
            maxLines = 2
        )
    }
}

