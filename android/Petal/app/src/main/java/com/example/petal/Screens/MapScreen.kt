package com.example.petal.Screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@Composable
fun MapScreen(navController: NavController){
    Column(modifier = Modifier
        .fillMaxSize()
        .background(Color(0xFFF6F5F2))
        .padding(16.dp)){

        Text(
            text = "Map",
            style = MaterialTheme.typography.headlineLarge,
            color = Color(0xFF3E2F26)
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = "Memories by location",
            fontSize = 14.sp,
            color = Color(0xFF7A6A5E)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(280.dp)
                .clip(RoundedCornerShape(24.dp))
                .background(Color(0xFFEDE6DE)),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Map will appear here",
                color = Color(0xFF9C8F86),
                fontSize = 14.sp
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Locations list
        Text(
            text = "PINNED PLACES",
            fontSize = 12.sp,
            letterSpacing = 1.sp,
            color = Color(0xFF9C8F86)
        )

        Spacer(modifier = Modifier.height(12.dp))

        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            LocationRow("Seaside Cafe")
            LocationRow("Home")
            LocationRow("College")
        }
    }

}
@Composable
private fun LocationRow(name: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = Color(0xFFF7F2ED),
                shape = RoundedCornerShape(16.dp)
            )
            .padding(14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = name,
            fontSize = 14.sp,
            color = Color(0xFF3E2F26)
        )
    }
}

@Preview
@Composable
fun MapScreenPreview(){
    //MapScreen()
}