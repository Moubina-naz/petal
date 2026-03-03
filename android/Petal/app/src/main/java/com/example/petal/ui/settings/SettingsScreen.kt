package com.example.petal.ui.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun SettingsScreen(
    onBack: () -> Unit = {},
    onEditProfile: () -> Unit = {},
    onLogout: () -> Unit = {}
) {

    var darkMode by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF7F3EE))
            .padding(20.dp)
    ) {

        Spacer(modifier = Modifier.height(10.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {

            IconButton(onClick = onBack) {
                Icon(Icons.Default.ArrowBack, contentDescription = null)
            }

            Text(
                text = "Settings",
                fontSize = 26.sp
            )
        }

        Spacer(modifier = Modifier.height(28.dp))

        Text(
            text = "Account",
            color = Color.Gray,
            fontSize = 14.sp
        )

        Spacer(modifier = Modifier.height(12.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White, RoundedCornerShape(12.dp))
                .clickable { onEditProfile() }
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Icon(Icons.Default.Person, contentDescription = null)

            Spacer(modifier = Modifier.width(12.dp))

            Text(
                text = "Edit Profile",
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White, RoundedCornerShape(12.dp))
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Icon(Icons.Default.DarkMode, contentDescription = null)

            Spacer(modifier = Modifier.width(12.dp))

            Text(
                text = "Dark Mode",
                modifier = Modifier.weight(1f)
            )

            Switch(
                checked = darkMode,
                onCheckedChange = { darkMode = it }
            )
        }

        Spacer(modifier = Modifier.height(28.dp))

        Text(
            text = "Security",
            color = Color.Gray,
            fontSize = 14.sp
        )

        Spacer(modifier = Modifier.height(12.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White, RoundedCornerShape(12.dp))
                .clickable { }
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Icon(Icons.Default.Lock, contentDescription = null)

            Spacer(modifier = Modifier.width(12.dp))

            Text(
                text = "Lock App",
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(40.dp))

        Button(
            onClick = { onLogout() },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFFD96C4F)
            )
        ) {

            Icon(Icons.Default.Logout, contentDescription = null)

            Spacer(modifier = Modifier.width(8.dp))

            Text("LOG OUT")
        }

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "PETAL v2.4.0 • FOCUS ON WHAT MATTERS",
            color = Color.Gray,
            fontSize = 11.sp,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
    }
}