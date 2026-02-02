package com.example.petal.ui.addMemory

import android.Manifest
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import coil.compose.AsyncImage
import com.example.petal.NavigationEvent


@Composable
fun AddMemoryScreen(
    viewModel: AddMemoryViewModel,
    onNavigationEvent: (NavigationEvent) -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current


    val pickerLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.PickMultipleVisualMedia(maxItems = 5)
    ) { uris ->
        viewModel.onImagesPicked(uris)
    }


    // Capture navigator during composition (fix @Composable invocation error)
    val navigator = LocalNavigator.currentOrThrow

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFDF8F4))
            .verticalScroll(rememberScrollState())
            .statusBarsPadding()
            .padding(16.dp)
    ) {
        // Top bar
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Cancel",
                style = MaterialTheme.typography.bodyMedium,
                color = Color(0xFF615A57),
                modifier = Modifier.clickable { onNavigationEvent(NavigationEvent.GoBack) }
            )

            Spacer(modifier = Modifier.weight(1f))

            Text(
                text = if (uiState.isSaving) "Saving..." else "Save",
                style = MaterialTheme.typography.bodyMedium,
                color = if (uiState.isSaving) Color.Gray else Color(0xFF9E6F73),
                modifier = Modifier.clickable(enabled = !uiState.isSaving) {
                    onNavigationEvent(NavigationEvent.SaveMemory)
                }
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Meta rows (DATE, TIME, LOCATION) — assume MetaRow is defined elsewhere
        MetaRow(label = "DATE", value = uiState.dateLabel)
        Spacer(modifier = Modifier.height(12.dp))
        MetaRow(label = "TIME", value = uiState.timeLabel)
        Spacer(modifier = Modifier.height(12.dp))
        MetaRow(
            label = "LOCATION",
            value = if (uiState.location.isEmpty()) "Add Location..." else uiState.location,
            valueColor = if (uiState.location.isEmpty()) Color(0xFFB0AAA3) else Color(0xFF615A57)
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Title
        BasicTextField(
            value = uiState.title,
            onValueChange = viewModel::onTitleChange,
            textStyle = MaterialTheme.typography.headlineLarge.copy(color = Color(0xFF3A3330)),
            modifier = Modifier.fillMaxWidth(),
            decorationBox = { inner ->
                if (uiState.title.isEmpty()) {
                    Text("Add a Title...", style = MaterialTheme.typography.headlineLarge, color = Color(0xFFB0AAA3))
                }
                inner()
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Mood button
        Box(
            modifier = Modifier
                .background(Color(0xFFEDE6DE), RoundedCornerShape(24.dp))
                .clickable { viewModel.onMoodClick() }
                .padding(horizontal = 14.dp, vertical = 10.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Add, null, tint = Color(0xFF615A57), modifier = Modifier.size(18.dp))
                Spacer(Modifier.width(8.dp))
                Text("How are you feeling?", fontSize = 14.sp, color = Color(0xFF615A57))
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Note
        BasicTextField(
            value = uiState.note,
            onValueChange = viewModel::onNoteChange,
            textStyle = androidx.compose.ui.text.TextStyle(fontSize = 16.sp, lineHeight = 26.sp, color = Color(0xFF3A3330)),
            modifier = Modifier.fillMaxWidth(),
            decorationBox = { inner ->
                if (uiState.note.isEmpty()) {
                    Text("Start writing your memory...", fontSize = 16.sp, lineHeight = 26.sp, color = Color(0xFFB0AAA3))
                }
                inner()
            }
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Show error if save failed
        uiState.error?.let { error ->
            Text(
                text = error,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(vertical = 8.dp)
            )
        }

        Text(
            text = "CAPTURED MOMENTS",
            fontSize = 12.sp,
            letterSpacing = 1.sp,
            color = Color(0xFF9C8F86)
        )

        Spacer(modifier = Modifier.height(12.dp))

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.heightIn(max = 420.dp)
        ) {
            items(uiState.images) { img ->
                Box(
                    modifier = Modifier
                        .aspectRatio(1f)
                        .clip(RoundedCornerShape(16.dp))
                        .background(Color(0xFFEDE6DE))
                        .clickable {
                            navigator.push(
                                MemoryImageGalleryScreen(
                                    images = uiState.images.map { it.localUri },
                                    initialIndex = uiState.images.indexOf(img)
                                )
                            )
                        }
                ) {
                    AsyncImage(
                        model = img.localUri,
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )

                    IconButton(
                        onClick = { viewModel.removeImage(img) },
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(8.dp)
                            .background(Color.White.copy(alpha = 0.7f), CircleShape)
                    ) {
                        Icon(Icons.Default.Close, null, tint = Color.Black)
                    }
                }
            }

            if (uiState.images.size < 5) {
                item {
                    Box(
                        modifier = Modifier
                            .aspectRatio(1f)
                            .clip(RoundedCornerShape(16.dp))
                            .border(2.dp, Color(0xFFD6CCC2), RoundedCornerShape(16.dp))
                            .clickable {
                                pickerLauncher.launch(
                                    PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
                                       },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.Add, null, tint = Color(0xFFB0AAA3))
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(32.dp))
    }
}