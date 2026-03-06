package com.example.petal.ui.homeDetailScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import coil.compose.AsyncImage
import com.example.petal.NavigationEvent
import com.example.petal.ui.addMemory.MemoryImageGalleryScreen
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@Composable
fun MemoryDetailScreen(
    viewModel: MemoryDetailViewModel,
    onNavigationEvent: (NavigationEvent) -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()
    var showMenu by remember { mutableStateOf(false) }

    var showViewer by remember { mutableStateOf(false) }
    var startIndex by remember { mutableStateOf(0) }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFDF8F4))
    ) {
        when (uiState) {
            is MemoryDetailUiState.Loading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            is MemoryDetailUiState.Error -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Something went wrong")
                }
            }

            is MemoryDetailUiState.Success -> {
                val memory = (uiState as MemoryDetailUiState.Success).memory

                val displayInstant = memory.memoryDateTime ?: memory.createdAt

                val dateText = remember(displayInstant) {
                    displayInstant
                        .atZone(ZoneId.systemDefault())
                        .format(DateTimeFormatter.ofPattern("MMM dd • hh:mm a"))
                }

                val locationText = remember(memory.location?.name) {
                    memory.location?.name ?: "Unknown location"
                }


                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .statusBarsPadding()
                        .padding(16.dp)
                ) {
                    // TOP BAR
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(onClick = { onNavigationEvent(NavigationEvent.GoBack) }) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Back"
                            )
                        }

                        Spacer(modifier = Modifier.weight(1f))

                        IconButton(onClick = { viewModel.toggleFavorite() }) {
                            Icon(
                                imageVector =
                                    if (memory.isFavorite)
                                        Icons.Default.Favorite
                                    else
                                        Icons.Default.FavoriteBorder,
                                tint =
                                    if (memory.isFavorite)
                                        Color(0xFFB07A7A)
                                    else
                                        Color(0xFF7A6A5E),
                                contentDescription = "Favorite"
                            )
                        }

                        Box {
                            IconButton(onClick = { showMenu = true }) {
                                Icon(Icons.Default.MoreVert, contentDescription = "More")
                            }

                            DropdownMenu(
                                expanded = showMenu,
                                onDismissRequest = { showMenu = false }
                            ) {
                                DropdownMenuItem(
                                    text = { Text("Delete") },
                                    onClick = {
                                        showMenu = false
                                        viewModel.deleteMemory {
                                            onNavigationEvent(NavigationEvent.GoBack)
                                        }
                                    }
                                )
                                DropdownMenuItem(
                                    text = { Text("Edit") },
                                    onClick = {
                                        showMenu = false
                                        onNavigationEvent(
                                            NavigationEvent.OpenEditMemory(memory.id)
                                        )
                                    }
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))


                    Text(
                        text = "$dateText · $locationText",
                        fontSize = 12.sp,
                        letterSpacing = 1.sp,
                        color = Color(0xFF9C8F86)
                    )


                    Spacer(modifier = Modifier.height(16.dp))


                    Text(
                        text = memory.title,
                        style = MaterialTheme.typography.headlineLarge,
                        color = Color(0xFF3E2F26)
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    memory.mood?.let { mood ->
                        Box(
                            modifier = Modifier
                                .background(
                                    color = Color(0xFFFCD1D1),
                                    shape = RoundedCornerShape(16.dp)
                                )
                                .padding(horizontal = 12.dp, vertical = 8.dp)
                        ) {
                            Text(
                                text = "✳ ${mood.label}",
                                fontSize = 12.sp,
                                color = Color(0xFF8A5A5A)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))


                    Text(
                        text = memory.note,
                        fontSize = 16.sp,
                        lineHeight = 26.sp,
                        color = Color(0xFF5C5048)
                    )


                    if (memory.images.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(32.dp))

                        Text(
                            text = "CAPTURED MOMENTS",
                            fontSize = 12.sp,
                            letterSpacing = 1.sp,
                            color = Color(0xFF9C8F86)
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        LazyVerticalGrid(
                            columns = GridCells.Fixed(2),
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp),
                            modifier = Modifier.heightIn(max = 420.dp)
                        ) {
                            items(memory.images) { image ->
                                if (image.imageUrl.isNotBlank()) {
                                    AsyncImage(
                                        model = image.imageUrl,
                                        contentDescription = null,
                                        contentScale = ContentScale.Crop,  // ← add this
                                        modifier = Modifier
                                            .aspectRatio(1f)
                                            .clip(RoundedCornerShape(16.dp))
                                            .clickable {
                                                startIndex = memory.images.indexOf(image)
                                                showViewer = true
                                            }
                                    )

                                }
                            }
                        }
                        if (showViewer) {
                            val navigator = LocalNavigator.currentOrThrow

                            LaunchedEffect(startIndex) {
                                navigator.push(
                                    MemoryImageGalleryScreen(
                                        images = memory.images.map { it.imageUrl },
                                        initialIndex = startIndex
                                    )
                                )
                                showViewer = false
                            }
                        }

                    }
                }
            }
        }
    }
}
@Preview(showBackground = true)
@Composable
fun MemoryDetailScreenPreview() {
    //MemoryDetailScreen()
}