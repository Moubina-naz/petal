package com.example.petal

import androidx.compose.foundation.background
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
import com.example.petal.Memory

@Composable
fun MemoryCard(
    memory : Memory
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .background(
                color = Color(0xFFF7F2ED),
                shape = RoundedCornerShape(16.dp)
            )
            .padding(16.dp)
    ) {
        Text(
            text = memory.title,
            style = MaterialTheme.typography.headlineMedium,
            color = Color(0xFF4E3A2F)
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = memory.note,
            style = MaterialTheme.typography.bodyMedium,
            color = Color(0xFF7A6A5E)
        )
    }
}
