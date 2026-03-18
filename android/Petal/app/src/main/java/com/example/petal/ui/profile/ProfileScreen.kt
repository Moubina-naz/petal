package com.example.petal.ui.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.SentimentSatisfied
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.petal.components.ErrorSnackbar
import com.example.petal.domain.Mood
import com.example.petal.ui.settings.SettingsItem
import com.example.petal.ui.settings.SettingsSectionHeader

@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel,
    onBack: () -> Unit = {},
    onEditProfile: () -> Unit,
    onLogOut: () -> Unit
) {
    val state by viewModel.uiState.collectAsState()
    var showLogoutDialog by remember { mutableStateOf(false) }


    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF7F3EE))
            .padding(horizontal = 20.dp)
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {

            Spacer(modifier = Modifier.height(50.dp))

            Text(
                text = state.username,
                fontSize = 32.sp,
                style = MaterialTheme.typography.headlineLarge,
                color = Color(0xFF2d2d2d) ,
                fontWeight = FontWeight.Normal,
                //fontFamily = FontFamily.Serif,
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

            Spacer(modifier = Modifier.height(30.dp))

            MoodCard(
                mood = state.dominantMood
            )

            Spacer(modifier = Modifier.height(24.dp))

            StreakCard(
                streak = state.streak
            )

            Spacer(modifier = Modifier.height(30.dp))

            SettingsSectionHeader("Account")

            Spacer(modifier = Modifier.height(16.dp))

            SettingsItem(
                icon = Icons.Default.Person,
                title = "Edit Profile",
                onClick = onEditProfile
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Log out
             Box(modifier = Modifier.fillMaxWidth(),
                 contentAlignment = Alignment.Center
             ){
                Button(
                    onClick = { showLogoutDialog = true },
                    modifier = Modifier
                        .wrapContentSize()
                        .padding(horizontal = 16.dp, vertical = 4.dp)
                        .height(52.dp)
                        .border(1.dp, Color(0xFF2d2d2d), RoundedCornerShape(24.dp)),

                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFCC6B4F))
                ) {
                    Icon(
                        Icons.AutoMirrored.Filled.ExitToApp,
                        contentDescription = null,
                        tint = Color.White
                    )
                    Spacer(Modifier.width(8.dp))
                    Text(
                        "LOG OUT", color = Color.White,
                        fontWeight = FontWeight.Bold, letterSpacing = 1.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(30.dp))


        }

        // Logout confirmation dialog
        if (showLogoutDialog) {
            AlertDialog(
                onDismissRequest = { showLogoutDialog = false },
                title = { Text("Log Out") },
                text = { Text("Are you sure you want to log out?") },
                confirmButton = {
                    TextButton(onClick = {
                        showLogoutDialog = false
                        onLogOut()
                    }) {
                        Text("Log Out", color = Color(0xFFCC6B4F), fontWeight = FontWeight.Bold)
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showLogoutDialog = false }) {
                        Text("Cancel")
                    }
                }
            )
        }
        val errorMessage by viewModel.errorMessage.collectAsState(initial = null)
        errorMessage?.let {
            ErrorSnackbar(message = it, onDismiss = { viewModel.clearError() })
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


        Text(
            text = value.toString(),
            fontSize = 22.sp,
            color = Color(0xFF2F2F2F),
            style = MaterialTheme.typography.headlineMedium,
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
    mood: Mood?
) {

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {

        Text(
            text = "Dominant Mood",
            style = MaterialTheme.typography.headlineMedium,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            color = Color.Gray
        )

        Spacer(modifier = Modifier.height(14.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .background(
                    color = Color(0xFFE7ECEA),
                    shape = RoundedCornerShape(50)
                )
                .padding(horizontal = 14.dp, vertical = 8.dp)
        ) {

            Icon(
                imageVector = mood?.icon ?: Icons.Outlined.SentimentSatisfied,
                contentDescription = null,
                tint = Color(0xFF4A6E66),
                modifier = Modifier.size(18.dp)
            )

            Spacer(modifier = Modifier.width(8.dp))

            Text(
                text = " $mood",
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF4A6E66)
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
            .padding(vertical = 4.dp),
    ) {

        Text(
            text = "Daily Commitment",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Medium,
            fontSize = 14.sp,
            letterSpacing = 1.sp,
            color = Color.Gray
        )

        Spacer(modifier = Modifier.height(18.dp))

        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxWidth(),
        ){
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(170.dp)
                    .border(
                        width = 2.dp,
                        color = Color(0xFFD96C4F),
                        shape = CircleShape
                    )
            ) {

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    Text(
                        text = streak.toString(),
                        fontSize = 42.sp,
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF2B2B2B)
                    )

                    Text(
                        text = "DAYS STREAK",
                        fontSize = 12.sp,
                        letterSpacing = 1.sp,
                        color = Color(0xFFD96C4F)
                    )
                }
            }
        }


    }
}