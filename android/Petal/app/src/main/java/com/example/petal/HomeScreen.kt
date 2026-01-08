package com.example.petal

import android.R.id.tabs
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)

@Composable
fun HomeScreen(modifier: Modifier = Modifier){

    Scaffold(
        containerColor = Color(0xFFFDF8F4),
        topBar = {
            Column(modifier = Modifier
                .fillMaxWidth().statusBarsPadding()
                .padding(horizontal = 16.dp, vertical = 12.dp)) {
                Row (modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ){
                    Text(
                        text = "Petal",
                        style = MaterialTheme.typography.headlineLarge,
                        color = Color(0xFF6B4F3F)
                    )

                    Spacer(modifier = Modifier.weight(1f))
                    Text(
                        "Oct 24",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.Black
                    )

                    Spacer(modifier = Modifier.width(8.dp))
                    IconButton(onClick = { /* later */ }) {
                        Icon(
                            imageVector = Icons.Default.Settings,
                            contentDescription = "Settings"
                        )
                    }
                }

                OutlinedTextField(
                    value = "",
                    onValueChange = {},
                    placeholder = { Text("Search memories...") },
                    leadingIcon = { Icon(Icons.Default.Search, null) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp),
                    shape = RoundedCornerShape(24.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = Color(0xFFF0EDE8),
                        unfocusedContainerColor = Color(0xFFF0EDE8),
                        disabledContainerColor = Color(0xFFF0EDE8),
                        errorContainerColor = Color(0xFFF0EDE8),

                        focusedBorderColor = Color.Transparent,
                        unfocusedBorderColor = Color.Transparent,
                        disabledBorderColor = Color.Transparent,
                        errorBorderColor = Color.Transparent
                    )
                )
                val tabs = listOf("All", "Favorites", "Photos", "Reflections")
                var selectedTab by rememberSaveable { mutableStateOf("All") }

                LazyRow(
                    modifier = Modifier.padding(top = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    contentPadding = PaddingValues(horizontal = 16.dp)
                ) {
                    items(tabs) { title ->
                        val isSelected = title == selectedTab

                        FilterChip(
                            onClick = { selectedTab = title },
                            label = {
                                Text(
                                    text = title,
                                    fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal
                                )
                            },
                            selected = isSelected,           // Required parameter
                            enabled = true,                  // Required parameter (almost always true)
                            colors = FilterChipDefaults.filterChipColors(
                                containerColor = Color.Transparent,
                                labelColor = Color.Gray,
                                selectedContainerColor = Color.Transparent,
                                selectedLabelColor = Color(0xFF6B4F3F)
                            ),
                            border = null // Clean look — no border at all
                        )
                    }
                }


            }
        },
        bottomBar = {},
        floatingActionButton = {},
        floatingActionButtonPosition = FabPosition.End,
    ) { innerPadding ->
        val sample = sampleSections

        LazyColumn(modifier = Modifier.padding(innerPadding),
           verticalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(bottom = 16.dp)) {

            sample.forEach { section ->
                item { SectionHeader(section.title) }

                items(section.memories) { memory ->
                    MemoryCard(memory)
                }
            }
        }


    }
}


@Preview(showBackground = true)
@Composable
fun HomeScreenPreview(){
    HomeScreen()
}