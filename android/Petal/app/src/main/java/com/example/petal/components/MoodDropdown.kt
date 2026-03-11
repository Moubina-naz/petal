package com.example.petal.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.petal.domain.Mood

@Composable
fun MoodDropdown(
    selectedMood: Mood?,
    onMoodSelected: (Mood) -> Unit
) {

    var expanded by remember { mutableStateOf(false) }

    Box {
        Box(
            modifier = Modifier
                .wrapContentWidth()
                .clip(RoundedCornerShape(24.dp))
                .background(Color(0xFFfffdf9))
                .border(1.dp, Color(0xFF2d2d2d), RoundedCornerShape(24.dp))
                .clickable { expanded = true }
                .padding(horizontal = 10.dp, vertical = 10.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Box(
                    modifier = Modifier
                        .size(26.dp)
                        .background(Color(0xFFfffdf9), CircleShape),
                    contentAlignment = Alignment.Center
                ){
                    Icon(
                        imageVector = selectedMood?.icon ?: Icons.Default.Add,
                        contentDescription = null,
                        tint = selectedMood?.color?: Color(0xFF615A57),
                        modifier = Modifier.size(18.dp)
                    )
                }
                Spacer(Modifier.width(10.dp))
                Text(
                    text = selectedMood?.label ?: "Mood?",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontSize = 14.sp,
                        color = Color(0xFF615A57)
                    )
                )
            }
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            containerColor = Color.Transparent,
            shadowElevation = 0.dp,
            offset = DpOffset(x = 0.dp, y = 8.dp),
            modifier = Modifier
                .width(180.dp)
                .heightIn(max = 210.dp)
                .background(Color(0xFFfffdf9))
                .border(1.dp, Color(0xFF2d2d2d), RoundedCornerShape(24.dp))
        ) {
            Mood.values().forEach { mood ->
                val isSelected = mood == selectedMood

                DropdownMenuItem(
                    text = {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(34.dp)
                                    .clip(CircleShape)
                                    .background(Color.White),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = mood.icon,
                                    contentDescription = null,
                                    tint = mood.color,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                            Spacer(Modifier.width(12.dp))
                            Text(
                                text = mood.label,
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontSize = 16.sp,
                                    color = Color(0xFF3A3330)
                                )
                            )
                            Spacer(Modifier.weight(1f))
                            if (isSelected) {
                                Icon(
                                    Icons.Default.Check,
                                    null,
                                    tint = Color(0xFF3A3330),
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }
                    },
                    onClick = {
                        onMoodSelected(mood)
                        expanded = false
                    },
                    colors = MenuDefaults.itemColors(
                        textColor = Color(0xFF3A3330),
                        leadingIconColor = mood.color
                    )
                )
            }
        }
    }
}
    @Composable
    fun MoodDropdownItem(
        mood: Mood,
        selected: Boolean,
        onClick: () -> Unit
    ) {
        val background =
            if (selected) Color(0xFFF6E7A8) // soft yellow highlight
            else Color.Transparent

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(18.dp))
                .background(background)
                .clickable { onClick() }
                .padding(horizontal = 12.dp, vertical = 10.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {

                Box(
                    modifier = Modifier
                        .size(34.dp)
                        .clip(CircleShape)
                        .background(Color.White),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = mood.icon,
                        contentDescription = null,
                        tint = Color(0xFF615A57),
                        modifier = Modifier.size(18.dp)
                    )
                }

                Spacer(Modifier.width(12.dp))

                Text(
                    text = mood.label,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontSize = 16.sp,
                        color = Color(0xFF3A3330)
                    )
                )

                Spacer(Modifier.weight(1f))

                if (selected) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = null,
                        tint =  mood.color,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }
    }



