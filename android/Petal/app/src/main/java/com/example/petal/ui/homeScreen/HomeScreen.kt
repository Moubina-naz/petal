package com.example.petal.ui.homeScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.petal.NavigationEvent
import com.example.petal.components.MemoryCard
import com.example.petal.components.SectionHeader
import com.example.petal.components.groupMemories

@OptIn(ExperimentalMaterial3Api::class)

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel,
    onNavigationEvent: (NavigationEvent) -> Unit = {}
) {
    val uiState = viewModel.uiState.collectAsState()
    var searchText by remember { mutableStateOf("") }


    val tabs = listOf("All", "Favorites", "Photos", "Reflections")
    var selectedTab by rememberSaveable { mutableStateOf("All") }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFFDF8F4))
    ) {
        // TOP BAR SECTION
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Petal",
                        style = MaterialTheme.typography.headlineLarge,
                        color = Color(0xFF6B4F3F)
                    )

                    Spacer(modifier = Modifier.padding(8.dp))
                    Text(
                        "Oct 24",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.Black
                    )
                }

                Spacer(modifier = Modifier.weight(1f))
                // Settings button placeholder
            }

            // SEARCH FIELD
            OutlinedTextField(
                value = searchText,
                onValueChange = {
                    searchText = it
                    viewModel.onSearchChange(it)
                },
                placeholder = { Text("Search memories...") },
                leadingIcon = {
                    Icon(Icons.Default.Search, null)
                },
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

            // FILTER CHIPS
            LazyRow(
                modifier = Modifier.padding(top = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(horizontal = 16.dp)
            ) {
                items(tabs) { title ->
                    val isSelected = title == selectedTab

                    FilterChip(
                        selected = isSelected,
                        onClick = {
                            selectedTab = title
                            viewModel.onFilterChange(
                                when (title) {
                                    "Favorites" -> HomeFilter.FAVORITES
                                    "Photos" -> HomeFilter.PHOTOS
                                    "Reflections" -> HomeFilter.REFLECTIONS
                                    else -> HomeFilter.ALL
                                }
                            )
                        },
                        label = {
                            Text(
                                title,
                                fontWeight = if (isSelected)
                                    FontWeight.SemiBold
                                else FontWeight.Normal
                            )
                        },
                        colors = FilterChipDefaults.filterChipColors(
                            containerColor = Color.Transparent,
                            selectedContainerColor = Color.Transparent,
                            labelColor = Color.Gray,
                            selectedLabelColor = Color(0xFF6B4F3F)
                        ),
                        border = null
                    )
                }
            }
        }

        // CONTENT SECTION (what was inside Scaffold content)
        when (uiState.value) {
            is HomeUiState.Loading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            is HomeUiState.Error -> {
                val message = (uiState.value as HomeUiState.Error).message
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = message)
                }
            }

            is HomeUiState.Success -> {
                val memories = (uiState.value as HomeUiState.Success).memories
                val sections = groupMemories(memories)

                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    contentPadding = PaddingValues(start = 16.dp,
                        end = 16.dp,
                        bottom = 96.dp )
                ) {
                    sections.forEach { section ->
                        item {
                            SectionHeader(section.title)
                        }

                        items(section.memories) { memory ->
                            MemoryCard(
                                memory = memory,
                                onMemoryClick = {
                                    onNavigationEvent(NavigationEvent.OpenMemoryDetail(memory))
                                },
                                onFavoriteClick = {
                                    onNavigationEvent(NavigationEvent.ToggleFavorite(memory))
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}
