package com.example.petal.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
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
    val timeText = instant
        .atZone(ZoneId.systemDefault())
        .format(DateTimeFormatter.ofPattern("hh:mm a"))

    val firstImage = memory.images
        .firstOrNull { it.imageUrl.contains("cloudinary") }
        ?.imageUrl
    val hasImage = firstImage != null

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(0.dp))
            .background(Color(0xFFFffffff))
            .border(1.dp, Color(0xFFB4B4B4), RoundedCornerShape(0.dp))
            .clickable { onMemoryClick() }
    ) {
        if (hasImage) {
            Row(
                modifier = Modifier.padding(12.dp),
                verticalAlignment = Alignment.Top
            ) {
                AsyncImage(
                    model = firstImage,
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(72.dp)
                        .clip(RoundedCornerShape(8.dp))
                )

                Spacer(modifier = Modifier.width(12.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.Top
                    ) {
                        Text(
                            text = memory.title,
                            style = MaterialTheme.typography.headlineMedium.copy(
                                fontWeight = FontWeight.Bold,
                                fontSize = 20.sp,
                                color = Color(0xFF3B2314)
                            ),
                            modifier = Modifier.weight(1f)
                        )
                        if (memory.isFavorite) {
                            Icon(
                                imageVector = Icons.Default.Favorite,
                                contentDescription = null,
                                tint = Color(0xFFCC6666),
                                modifier = Modifier
                                    .size(16.dp)

                            )
                        }
                    }

                    if (memory.note.isNotBlank()) {
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = memory.note,
                            style = MaterialTheme.typography.bodyMedium.copy(
                                color = Color(0xFF9C8F86),
                                fontSize = 14.sp
                            ),
                            maxLines = 2
                        )
                    }

                    // ── Mood + tags (same as no-image branch) ──
                    val hasMoodOrTags = memory.mood != null || memory.tags.isNotEmpty()
                    if (hasMoodOrTags) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            memory.mood?.let { mood ->
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(5.dp),
                                    modifier = Modifier
                                        .background(
                                            color = mood.color.copy(alpha = 0.08f),
                                            shape = RoundedCornerShape(20.dp)
                                        )
                                        .padding(horizontal = 10.dp, vertical = 4.dp)
                                ) {
                                    Icon(
                                        imageVector = mood.icon,
                                        contentDescription = mood.label,
                                        tint = mood.color,
                                        modifier = Modifier.size(13.dp)
                                    )
                                    Text(
                                        text = mood.label.uppercase(),
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = mood.color,
                                        letterSpacing = 0.6.sp
                                    )
                                }
                            }

                            memory.tags.take(2).forEach { tag ->
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(5.dp),
                                    modifier = Modifier
                                        .background(
                                            color = Color(0xFFEEE8E0),
                                            shape = RoundedCornerShape(20.dp)
                                        )
                                        .padding(horizontal = 10.dp, vertical = 4.dp)
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(6.dp)
                                            .background(Color(0xFFB09A88), CircleShape)
                                    )
                                    Text(
                                        text = tag.uppercase(),
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = Color(0xFF8B7060),
                                        letterSpacing = 0.6.sp
                                    )
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.width(8.dp))
            }

        } else {
            //no image in memory

            Column(modifier = Modifier.padding(16.dp)) {

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Top
                ) {
                    Text(
                        text = memory.title,
                        style = MaterialTheme.typography.headlineMedium.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp,
                            color = Color(0xFF3B2314)
                        ),
                        modifier = Modifier.weight(1f)
                    )

                    Column(horizontalAlignment = Alignment.End) {
                        Text(
                            text = timeText,
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = Color(0xFF8e8e8e),
                                fontSize = 11.sp
                            )
                        )
                        if (memory.isFavorite) {
                            Spacer(modifier = Modifier.height(4.dp))
                            Icon(
                                imageVector = Icons.Default.Favorite,
                                contentDescription = null,
                                tint = Color(0xFFCC6666),
                                modifier = Modifier
                                    .size(14.dp)
                                    .clickable { onFavoriteClick() }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Note
                if (memory.note.isNotBlank()) {
                    Text(
                        text = memory.note,
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = Color(0xFF8e8e8e),
                            fontSize = 14.sp,
                            lineHeight = 20.sp
                        ),
                        maxLines = 4
                    )
                }

                // Mood
                val hasMoodOrTags = memory.mood != null || memory.tags.isNotEmpty()
                if (hasMoodOrTags) {
                    Spacer(modifier = Modifier.height(12.dp))
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Mood
                        memory.mood?.let { mood ->
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(5.dp),
                                modifier = Modifier
                                    .background(
                                        color = mood.color.copy(alpha = 0.08f),
                                        shape = RoundedCornerShape(20.dp)
                                    )
                                    .padding(horizontal = 10.dp, vertical = 4.dp)
                            ) {
                                Icon(
                                    imageVector = mood.icon,
                                    contentDescription = mood.label,
                                    tint = mood.color,
                                    modifier = Modifier.size(13.dp)
                                )
                                Text(
                                    text = mood.label.uppercase(),
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = mood.color,
                                    letterSpacing = 0.6.sp
                                )
                            }
                        }

                        // Tags (unchanged)
                        memory.tags.take(2).forEach { tag ->
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(5.dp),
                                modifier = Modifier
                                    .background(
                                        color = Color(0xFFEEE8E0),
                                        shape = RoundedCornerShape(20.dp)
                                    )
                                    .padding(horizontal = 10.dp, vertical = 4.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(6.dp)
                                        .background(Color(0xFFB09A88), CircleShape)
                                )
                                Text(
                                    text = tag.uppercase(),
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = Color(0xFF8B7060),
                                    letterSpacing = 0.6.sp
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}