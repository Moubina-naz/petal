package com.example.petal.ui.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun SettingsScreen(
    onBack: () -> Unit,
    onEditProfile: () -> Unit,
    onLogOut: () -> Unit
) {
    var showLogoutDialog by remember { mutableStateOf(false) }
    val bg = Color(0xFFF7F3EE)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(bg)
            .statusBarsPadding()
    ) {
        // Top bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBack) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
            }
            Text(
                text = "Settings",
                fontSize = 20.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF2F2F2F)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Account section
        SettingsSectionHeader("Account")

        SettingsItem(
            icon = Icons.Default.Person,
            title = "Edit Profile",
            onClick = onEditProfile
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Log out button
        Button(
            onClick = { showLogoutDialog = true },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .height(52.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFCC6B4F))
        ) {
            Icon(Icons.AutoMirrored.Filled.ExitToApp, contentDescription = null, tint = Color.White)
            Spacer(Modifier.width(8.dp))
            Text("LOG OUT", color = Color.White, fontWeight = FontWeight.Bold, letterSpacing = 1.sp)
        }

        Spacer(modifier = Modifier.weight(1f))

        Text(
            text = "PETAL • FOCUS ON WHAT MATTERS",
            fontSize = 11.sp,
            color = Color.Gray,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(bottom = 24.dp)
        )
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
}

@Composable
fun SettingsSectionHeader(title: String) {
    Text(
        text = title,
        fontSize = 14.sp,
        style = MaterialTheme.typography.headlineMedium,
        color = Color.Gray,
        modifier = Modifier.padding( vertical = 8.dp)
    )
}

@Composable
fun SettingsItem(
    icon: ImageVector,
    title: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 4.dp)
            .background(Color.White)
            .border(1.dp, Color(0xFF2d2d2d))
            .clickable { onClick() }
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Spacer(Modifier.width(12.dp))
        Text(title,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.weight(1f),
            color = Color(0xFF2F2F2F))
        Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = null, tint = Color.LightGray)
    }
}