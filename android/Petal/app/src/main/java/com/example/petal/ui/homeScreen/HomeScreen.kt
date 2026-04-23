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
import com.example.petal.theme.extended

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

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
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
                        color = MaterialTheme.colorScheme.onBackground           )
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
                            color = MaterialTheme.extended.textSecondary,
                            fontSize = 14.sp
                        )
                    },
                    leadingIcon = {
                        Icon(
                            Icons.Default.Search,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.size(18.dp)
                        )
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(28.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = MaterialTheme.colorScheme.surface,
                        unfocusedContainerColor = MaterialTheme.colorScheme.background,
                        focusedBorderColor = MaterialTheme.colorScheme.outlineVariant,
                        unfocusedBorderColor = Color.Transparent,
                        focusedTextColor = MaterialTheme.colorScheme.onSurface,
                        unfocusedTextColor = MaterialTheme.colorScheme.onSurface
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
                                        "Favourites" -> HomeFilter.FAVORITES
                                        "Photos"     -> HomeFilter.PHOTOS
                                        else         -> HomeFilter.ALL
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
                                containerColor = MaterialTheme.colorScheme.surface,
                                selectedContainerColor = MaterialTheme.colorScheme.primary,
                                labelColor = MaterialTheme.colorScheme.onBackground,
                                selectedLabelColor = MaterialTheme.colorScheme.onPrimary
                            ),
                            border = FilterChipDefaults.filterChipBorder(
                                enabled = true,
                                selected = isSelected,
                                borderColor = MaterialTheme.colorScheme.outline,
                                selectedBorderColor = MaterialTheme.colorScheme.outlineVariant,
                                borderWidth = 1.dp,
                                selectedBorderWidth = 1.dp
                            )
                        )
                    }
                }
                Spacer(modifier = Modifier.height(2.dp))
            }

            // Content area
            when (uiState) {
                is HomeUiState.Loading -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
                    }
                }
                is HomeUiState.Error -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text(
                            (uiState as HomeUiState.Error).message,
                            color = MaterialTheme.colorScheme.outline
                        )
                    }
                }
                is HomeUiState.Success -> {
                    val memories = (uiState as HomeUiState.Success).memories

                    if (selectedTab == "Photos") {
                        val allImages = memories.flatMap { memory ->
                            memory.images.map { image -> Pair(image, memory) }
                        }

                        if (allImages.isEmpty()) {
                            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                                Text(
                                    "No photos yet",
                                    color = MaterialTheme.colorScheme.outline
                                )
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
                                                        Text(
                                                            "Go to Memory",
                                                            color = Color.White,
                                                            fontWeight = FontWeight.Bold
                                                        )
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
                            contentPadding = PaddingValues(
                                start = 16.dp, end = 16.dp,
                                top = 8.dp, bottom = 100.dp
                            )
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

                else -> {}
            }
        }

        // FAB
        FloatingActionButton(
            onClick = { onNavigationEvent(NavigationEvent.OpenAddMemory()) },
            containerColor = MaterialTheme.colorScheme.primary,
            shape = CircleShape,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(20.dp)
                .size(52.dp)
                .border(1.dp, MaterialTheme.colorScheme.outlineVariant, CircleShape)
        ) {
            Icon(
                Icons.Default.Add,
                contentDescription = "Add Memory",
                tint = MaterialTheme.colorScheme.onPrimary
            )
        }

        errorMessage?.let {
            ErrorSnackbar(message = it, onDismiss = { viewModel.clearError() })
        }
    }
}