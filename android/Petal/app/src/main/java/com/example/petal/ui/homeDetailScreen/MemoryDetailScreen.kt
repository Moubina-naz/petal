package com.example.petal.ui.homeDetailScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardReturn
import androidx.compose.material.icons.filled.ArrowBackIosNew
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
import com.example.petal.components.ErrorSnackbar
import com.example.petal.ui.addMemory.MemoryImageGalleryScreen
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MenuDefaults
import androidx.compose.ui.unit.DpOffset
import com.example.petal.components.UrlAudioBar

@Composable
fun MemoryDetailScreen(
    viewModel: MemoryDetailViewModel,
    onNavigationEvent: (NavigationEvent) -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()
    var showMenu by remember { mutableStateOf(false) }

    var showViewer by remember { mutableStateOf(false) }
    var startIndex by remember { mutableStateOf(0) }
    val errorMessage by viewModel.errorMessage.collectAsState()

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
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
                        memory.location?.name ?: "Unknown Location"
                    }


                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState())
                            .statusBarsPadding()
                            .padding(16.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            IconButton(onClick = { onNavigationEvent(NavigationEvent.GoBack) }) {
                                Icon(
                                    imageVector = Icons.Filled.ArrowBackIosNew,
                                    contentDescription = "Back"
                                )
                            }

                            Spacer(modifier = Modifier.weight(1f))
                            //favorite icon

                            IconButton(onClick = { viewModel.toggleFavorite() }) {
                                Icon(
                                    imageVector =
                                        if (memory.isFavorite)
                                            Icons.Default.Favorite
                                        else
                                            Icons.Default.FavoriteBorder,
                                    tint =
                                        if (memory.isFavorite)
                                            MaterialTheme.colorScheme.error
                                        else
                                            MaterialTheme.colorScheme.onBackground,
                                    contentDescription = "Favorite"
                                )
                            }


                            var showDeleteDialog by remember { mutableStateOf(false) }

                            Box {
                                IconButton(onClick = { showMenu = true }) {
                                    Icon(Icons.Default.MoreVert, contentDescription = "More")
                                }

                                DropdownMenu(
                                    expanded = showMenu,
                                    onDismissRequest = { showMenu = false },
                                    containerColor = MaterialTheme.colorScheme.surface,
                                    shadowElevation = 0.dp,
                                    offset = DpOffset(x = 0.dp, y = 8.dp),
                                    modifier = Modifier
                                        .width(160.dp)
                                        .border(
                                            1.dp,
                                            MaterialTheme.colorScheme.outline,
                                            RoundedCornerShape(24.dp)
                                        )
                                ) {
                                    // Edit
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
                                                        .background(MaterialTheme.colorScheme.surfaceVariant),
                                                    contentAlignment = Alignment.Center
                                                ) {
                                                    Icon(
                                                        imageVector = Icons.Default.Edit,
                                                        contentDescription = null,
                                                        tint = MaterialTheme.colorScheme.onSurface,
                                                        modifier = Modifier.size(18.dp)
                                                    )
                                                }
                                                Spacer(Modifier.width(12.dp))
                                                Text(
                                                    text = "Edit",
                                                    style = MaterialTheme.typography.titleMedium.copy(
                                                        fontSize = 16.sp,
                                                        color = MaterialTheme.colorScheme.onSurface
                                                    )
                                                )
                                            }
                                        },
                                        onClick = {
                                            showMenu = false
                                            onNavigationEvent(NavigationEvent.OpenEditMemory(memory.id))
                                        },
                                        colors = MenuDefaults.itemColors(textColor = Color(0xFF3A3330))
                                    )

                                    // Delete
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
                                                        .background(MaterialTheme.colorScheme.errorContainer),
                                                    contentAlignment = Alignment.Center
                                                ) {
                                                    Icon(
                                                        imageVector = Icons.Default.Delete,
                                                        contentDescription = null,
                                                        tint = MaterialTheme.colorScheme.error,
                                                        modifier = Modifier.size(18.dp)
                                                    )
                                                }
                                                Spacer(Modifier.width(12.dp))
                                                Text(
                                                    text = "Delete",
                                                    style = MaterialTheme.typography.titleMedium.copy(
                                                        fontSize = 16.sp,
                                                        color = MaterialTheme.colorScheme.error
                                                    )
                                                )
                                            }
                                        },
                                        onClick = {
                                            showMenu = false
                                            showDeleteDialog = true
                                        },
                                        colors = MenuDefaults.itemColors(textColor = Color(0xFFCC6666))
                                    )
                                }
                            }

                            if (showDeleteDialog) {
                                AlertDialog(
                                    onDismissRequest = { showDeleteDialog = false },
                                    containerColor = MaterialTheme.colorScheme.surface,
                                    shape = RoundedCornerShape(24.dp),
                                    title = {
                                        Text(
                                            text = "Delete memory?",
                                            style = MaterialTheme.typography.titleMedium.copy(
                                                fontSize = 18.sp,
                                                color = MaterialTheme.colorScheme.onSurface                                            )
                                        )
                                    },
                                    text = {
                                        Text(
                                            text = "This memory will be permanently deleted and cannot be recovered.",
                                            style = MaterialTheme.typography.bodyMedium.copy(
                                                fontSize = 14.sp,
                                                lineHeight = 22.sp,
                                                color = MaterialTheme.colorScheme.onSurfaceVariant                                            )
                                        )
                                    },
                                    confirmButton = {
                                        Box(
                                            modifier = Modifier
                                                .clip(RoundedCornerShape(20.dp))
                                                .background(MaterialTheme.colorScheme.error)
                                                .clickable {
                                                    showDeleteDialog = false
                                                    viewModel.deleteMemory {
                                                        onNavigationEvent(NavigationEvent.GoBack)
                                                    }
                                                }
                                                .padding(horizontal = 20.dp, vertical = 10.dp)
                                        ) {
                                            Text(
                                                text = "Delete",
                                                style = MaterialTheme.typography.titleMedium.copy(
                                                    fontSize = 14.sp,
                                                    color = MaterialTheme.colorScheme.onPrimary                                                )
                                            )
                                        }
                                    },
                                    dismissButton = {
                                        Box(
                                            modifier = Modifier
                                                .clip(RoundedCornerShape(20.dp))
                                                .border(1.dp,     MaterialTheme.colorScheme.outline, RoundedCornerShape(20.dp))
                                                .clickable { showDeleteDialog = false }
                                                .padding(horizontal = 20.dp, vertical = 10.dp)
                                        ) {
                                            Text(
                                                text = "Cancel",
                                                style = MaterialTheme.typography.titleMedium.copy(
                                                    fontSize = 14.sp,
                                                    color = MaterialTheme.colorScheme.onBackground

                                                )
                                            )
                                        }
                                    }
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(24.dp))


                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {

                            Box(
                                modifier = Modifier
                                    .width(30.dp)
                                    .height(1.dp)
                                    .background(MaterialTheme.colorScheme.primary)
                            )

                            Spacer(modifier = Modifier.width(10.dp))

                            Text(
                                text = "$dateText • $locationText",
                                fontSize = 14.sp,
                                letterSpacing = 1.sp,

                                color = MaterialTheme.colorScheme.onBackground

                            )
                        }


                        Spacer(modifier = Modifier.height(20.dp))


                        Text(
                            text = memory.title,
                            style = MaterialTheme.typography.headlineLarge,
                            color = MaterialTheme.colorScheme.onBackground
                        )

                        Spacer(modifier = Modifier.height(20.dp))


                        //MOOOOD
                        memory.mood?.let { mood ->
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp),
                                modifier = Modifier
                                    .background(
                                        color = MaterialTheme.colorScheme.background,
                                        shape = RoundedCornerShape(20.dp)
                                    )
                                    .border(1.dp, MaterialTheme.colorScheme.outlineVariant, RoundedCornerShape(20.dp))
                                    .padding(horizontal = 12.dp, vertical = 8.dp)
                            ) {
                                Icon(
                                    imageVector = mood.icon,
                                    contentDescription = mood.label,
                                    tint = mood.color,
                                    modifier = Modifier.size(15.dp)
                                )
                                Text(
                                    text = mood.label,
                                    fontSize = 14.sp,
                                    color = MaterialTheme.colorScheme.onBackground
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(20.dp))

                        memory.audioUrl?.let { url ->
                            Spacer(modifier = Modifier.height(16.dp))
                            UrlAudioBar(url = url)
                        }

                        Spacer(modifier = Modifier.height(20.dp))



                        Text(
                            text = memory.note,
                            fontSize = 16.sp,
                            lineHeight = 26.sp,
                            color =  MaterialTheme.colorScheme.onSurfaceVariant
                        )


                        if (memory.images.isNotEmpty()) {
                            Spacer(modifier = Modifier.height(28.dp))

                            Text(
                                text = "CAPTURED MOMENTS",
                                fontSize = 12.sp,
                                letterSpacing = 1.sp,
                                color =MaterialTheme.colorScheme.onSurface
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
        errorMessage?.let {
            ErrorSnackbar(
                message   = it,
                onDismiss = { viewModel.clearError() }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MemoryDetailScreenPreview() {
    //MemoryDetailScreen()
}