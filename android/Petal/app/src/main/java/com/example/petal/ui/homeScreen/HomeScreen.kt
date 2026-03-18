package com.example.petal.ui.homeScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.petal.NavigationEvent
import com.example.petal.components.MemoryCard
import com.example.petal.components.SectionHeader
import com.example.petal.components.groupMemories
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import coil.compose.AsyncImage
import com.example.petal.components.ErrorSnackbar

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel,
    onNavigationEvent: (NavigationEvent) -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()
    var searchText by remember { mutableStateOf("") }

    val tabs = listOf("All Moments", "Favourites", "Photos")
    var selectedTab by rememberSaveable { mutableStateOf("All Moments") }
    val focusManager = LocalFocusManager.current
    val errorMessage by viewModel.errorMessage.collectAsState()

    val bg = Color(0xFFFf9f7f2)
    val black = Color(0xFF2d2d2d)
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(bg)
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ) {
                focusManager.clearFocus()
            }
    ) {
        Column(modifier = Modifier.fillMaxSize()) {

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding()
                    .padding(horizontal = 16.dp)
                    .padding(top = 2.dp)
            ) {
                // Title row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Petal",
                        style = MaterialTheme.typography.headlineLarge,
                        fontWeight = FontWeight.Normal,
                        color = Color(0xFF2d2d2d)
                    )

                }

                Spacer(modifier = Modifier.height(10.dp))

                // Search bar

                    OutlinedTextField(
                        value = searchText,
                        onValueChange = {
                            searchText = it
                            viewModel.onSearchChange(it)
                        },
                        placeholder = {
                            Text(
                                "Search memories...",
                                color = Color(0xFFB0A89E),
                                fontSize = 14.sp
                            )
                        },
                        leadingIcon = {
                            Icon(
                                Icons.Default.Search,
                                contentDescription = null,
                                tint = Color(0xFFB0A89E),
                                modifier = Modifier.size(18.dp)
                            )
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(28.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = Color(0xFFFFFFFF),
                            unfocusedContainerColor = bg,
                            focusedBorderColor = Color(0xFF6E625A),   // softer than black
                            unfocusedBorderColor = Color.Transparent,
                            focusedTextColor = Color(0xFF2B1A10),
                            unfocusedTextColor = Color(0xFF2B1A10)
                        ),
                        singleLine = true
                    )


                Spacer(modifier = Modifier.height(12.dp))

                // Filter chips
                LazyRow(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    items(tabs) { title ->
                        val isSelected = title == selectedTab
                        FilterChip(
                            selected = isSelected,
                            onClick = {
                                selectedTab = title
                                viewModel.onFilterChange(
                                    when (title) {
                                        "Favourites" -> HomeFilter.FAVORITES  // ← match exact tab name
                                        "Photos" -> HomeFilter.PHOTOS
                                        else -> HomeFilter.ALL
                                    }
                                )
                            },
                            label = {
                                Text(
                                    title,
                                    fontSize = 15.sp,
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
                                    modifier = Modifier.padding(vertical = 4.dp, horizontal = 2.dp)
                                )
                            },
                            shape = RoundedCornerShape(20.dp),
                            colors = FilterChipDefaults.filterChipColors(
                                containerColor = Color(0xFFffffff),
                                selectedContainerColor = Color(0xFFd36b54),
                                labelColor = black,
                                selectedLabelColor = Color.White
                            ),
                            border = FilterChipDefaults.filterChipBorder(
                                enabled = true, selected = isSelected,
                                borderColor = Color(0xFF98948d),
                                selectedBorderColor = black,
                                borderWidth = 1.dp,
                                selectedBorderWidth = 1.dp,

                            )
                        )
                    }
                }
                Spacer(modifier = Modifier.height(2.dp))
            }

            when (uiState) {
                is HomeUiState.Loading -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(color = black)
                    }
                }
                is HomeUiState.Error -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text((uiState as HomeUiState.Error).message, color = Color(0xFF9C8F86))
                    }
                }
                is HomeUiState.Success -> {
                    val memories = (uiState as HomeUiState.Success).memories

                    if (selectedTab == "Photos") {
                        // Flatten all images from all memories
                        val allImages = memories.flatMap { memory ->
                            memory.images.map { image -> Pair(image, memory) }
                        }

                        if (allImages.isEmpty()) {
                            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                                Text("No photos yet", color = Color(0xFF9C8F86))
                            }
                        } else {
                            LazyVerticalGrid(
                                columns = GridCells.Fixed(3),
                                modifier = Modifier.fillMaxSize(),
                                contentPadding = PaddingValues(4.dp),
                                horizontalArrangement = Arrangement.spacedBy(2.dp),
                                verticalArrangement = Arrangement.spacedBy(2.dp)
                            ) {
                                items(allImages) { (image, memory) ->
                                    var showOptions by remember { mutableStateOf(false) }

                                    Box(
                                        modifier = Modifier
                                            .aspectRatio(1f)
                                            .clickable { showOptions = true }
                                    ) {
                                        AsyncImage(
                                            model = image.imageUrl,
                                            contentDescription = null,
                                            contentScale = ContentScale.Crop,
                                            modifier = Modifier.fillMaxSize()
                                        )

                                        if (showOptions) {
                                            Box(
                                                modifier = Modifier
                                                    .fillMaxSize()
                                                    .background(Color.Black.copy(alpha = 0.5f))
                                                    .clickable { showOptions = false },
                                                contentAlignment = Alignment.Center
                                            ) {
                                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                                    TextButton(onClick = {
                                                        showOptions = false
                                                        onNavigationEvent(NavigationEvent.OpenMemoryDetail(memory.id))
                                                    }) {
                                                        Text("Go to Memory", color = Color.White, fontWeight = FontWeight.Bold)
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    } else {
                        val sections = groupMemories(memories)
                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 100.dp)
                        ) {
                            sections.forEach { section ->
                                item {
                                    Spacer(modifier = Modifier.height(16.dp))
                                    SectionHeader(section.title)
                                    Spacer(modifier = Modifier.height(8.dp))
                                }
                                items(section.memories) { memory ->
                                    MemoryCard(
                                        memory = memory,
                                        onMemoryClick = { onNavigationEvent(NavigationEvent.OpenMemoryDetail(memory.id)) },
                                        onFavoriteClick = { onNavigationEvent(NavigationEvent.ToggleFavorite(memory.id)) }
                                    )
                                    Spacer(modifier = Modifier.height(10.dp))
                                }
                            }
                        }
                    }
                }
            }
        }


        FloatingActionButton(
            onClick = { onNavigationEvent(NavigationEvent.OpenAddMemory()) },
            containerColor = Color(0xFFd36b54),
            shape = CircleShape,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(20.dp)
                .size(52.dp)
                .border(1.dp, black, CircleShape)
        ) {
            Icon(Icons.Default.Add, contentDescription = "Add Memory", tint = Color.White)
        }
        errorMessage?.let {
            ErrorSnackbar(message = it, onDismiss = { viewModel.clearError() })
        }
    }
}
