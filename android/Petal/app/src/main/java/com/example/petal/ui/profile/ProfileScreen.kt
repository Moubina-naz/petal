package com.example.petal.ui.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel,
    onBack: () -> Unit = {},
    onSettings: () -> Unit = {}
) {
    val state by viewModel.uiState.collectAsState()
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF7F3EE))
            .padding(horizontal = 20.dp)
    ) {

        Column(
            modifier = Modifier.fillMaxSize()
        ) {

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {

                IconButton(onClick = onBack) {
                    Icon(Icons.Default.ArrowBack, contentDescription = null)
                }

                Spacer(modifier = Modifier.weight(1f))

                IconButton(onClick = onSettings) {
                    Icon(Icons.Default.Settings, contentDescription = null)
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = state.username,
                fontSize = 32.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF2F2F2F),
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )


            Spacer(modifier = Modifier.height(28.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {

                StatCard(
                    icon = Icons.Default.Book,
                    value = state.totalMemories,
                    label = "TOTAL"
                )

                StatCard(
                    icon = Icons.Default.Image,
                    value = state.totalPhotos,
                    label = "PHOTOS"
                )

                StatCard(
                    icon = Icons.Default.Mic,
                    value = state.totalVoice,
                    label = "VOICE"
                )
            }

            Spacer(modifier = Modifier.height(28.dp))

            MoodCard(
                mood = state.dominantMood
            )

            Spacer(modifier = Modifier.height(32.dp))

            StreakCard(
                streak = state.streak
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "END OF RECORDS",
                fontSize = 11.sp,
                color = Color.Gray,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
        }
    }
}

@Composable
private fun StatCard(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    value: Int,
    label: String
) {

    Column(
        modifier = Modifier
            .width(100.dp)
            .border(1.dp, Color.LightGray)
            .padding(vertical = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Icon(icon, contentDescription = null, tint = Color.Gray)

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = value.toString(),
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = label,
            fontSize = 11.sp,
            color = Color.Gray
        )
    }
}

@Composable
private fun MoodCard(
    mood: String
) {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, Color.LightGray)
            .padding(18.dp)
    ) {

        Text(
            text = "MOOD SNAPSHOT",
            fontSize = 11.sp,
            color = Color.Gray
        )

        Spacer(modifier = Modifier.height(10.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {

            Text(
                text = "⚙",
                fontSize = 18.sp
            )

            Spacer(modifier = Modifier.width(10.dp))

            Text(
                text = "Dominant Mood:",
                color = Color.Gray
            )

            Spacer(modifier = Modifier.width(6.dp))

            Text(
                text = mood,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
private fun StreakCard(
    streak: Int
) {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .border(
                width = 2.dp,
                color = Color(0xFFD96C4F),
                shape = RoundedCornerShape(4.dp)
            )
            .padding(vertical = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            text = "MEMORY STREAK",
            fontSize = 12.sp,
            color = Color(0xFFD96C4F),
            fontWeight = FontWeight.Medium
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = streak.toString(),
            fontSize = 40.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFFD96C4F)
        )

        Text(
            text = "DAYS",
            fontSize = 12.sp,
            color = Color.Gray
        )
    }
}