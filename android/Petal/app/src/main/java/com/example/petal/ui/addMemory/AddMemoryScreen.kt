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
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.PieChartOutline
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import coil.compose.AsyncImage
import com.example.petal.NavigationEvent
import com.example.petal.components.FullScreenImageViewer
import com.example.petal.components.MoodDropdown
import com.example.petal.domain.Mood
import java.nio.file.Paths.get
import java.time.Instant
import java.time.LocalTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddMemoryScreen(
    viewModel: AddMemoryViewModel,
    onNavigationEvent: (NavigationEvent) -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    var showDatePicker by remember { mutableStateOf(false) }
    var showTimePicker by remember { mutableStateOf(false) }

    val pickerLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.PickMultipleVisualMedia(maxItems = 5)
    ) { uris ->
        viewModel.onImagesPicked(uris)
    }
    var showViewer by remember { mutableStateOf(false) }
    var startIndex by remember { mutableStateOf(0) }
    val navigator = LocalNavigator.currentOrThrow
    var showLocationSheet by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFDF8F4))
            .verticalScroll(rememberScrollState())
            .statusBarsPadding()
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "Cancel",
                style = MaterialTheme.typography.bodyMedium,
                color = Color(0xFF615A57),
                modifier = Modifier.clickable { onNavigationEvent(NavigationEvent.GoBack) })
            Spacer(modifier = Modifier.weight(1f))

            Text(text = if (uiState.isSaving) "Saving..." else "Save",
                style = MaterialTheme.typography.bodyMedium,
                color = if (uiState.isSaving) Color.Gray else Color(0xFF9E6F73),
                modifier = Modifier.clickable(enabled = !uiState.isSaving) {
                    onNavigationEvent(NavigationEvent.SaveMemory)
                }
            )
        }


         Spacer(modifier = Modifier.height(12.dp))


        OutlinedTextField(
            value = uiState.selectedDate.format(DateTimeFormatter.ofPattern("MMMM d, yyyy").withLocale(java.util.Locale.US)),
            onValueChange = {},
            readOnly = true,
            label = { Text("Date") },
            trailingIcon = {
                IconButton(onClick = { showDatePicker = true }) {
                    Icon(
                        imageVector = Icons.Default.DateRange,
                        contentDescription = "Select date",
                        tint = Color(0xFF615A57)
                    )
                }
            },
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = Color(0xFF3A3330),
                unfocusedTextColor = Color(0xFF3A3330)
            )
        )

        if (showDatePicker) {
            val initialMillis = uiState.selectedDate
                .atStartOfDay(java.time.ZoneId.systemDefault())
                .toInstant()
                .toEpochMilli()

            val datePickerState = rememberDatePickerState(initialSelectedDateMillis = initialMillis)

            DatePickerDialog(
                onDismissRequest = { showDatePicker = false },
                confirmButton = {
                    TextButton(onClick = {
                        datePickerState.selectedDateMillis?.let { millis ->
                            val selected = java.time.Instant.ofEpochMilli(millis)
                                .atZone(java.time.ZoneId.systemDefault())
                                .toLocalDate()
                            viewModel.onDateSelected(selected)
                        }
                        showDatePicker = false
                    }) { Text("OK") }
                },
                dismissButton = {
                    TextButton(onClick = { showDatePicker = false }) { Text("Cancel") }
                }
            ) {
                DatePicker(state = datePickerState)
            }
        }

        Spacer(Modifier.height(16.dp))

        // Time
        OutlinedTextField(
            value = uiState.selectedTime.format(DateTimeFormatter.ofPattern("hh:mm a")),
            onValueChange = {},
            readOnly = true,
            label = { Text("Time") },
            trailingIcon = {
                IconButton(onClick = { showTimePicker = true }) {
                    Icon(
                        imageVector = Icons.Default.Schedule,
                        contentDescription = "Select time",
                        tint = Color(0xFF615A57)
                    )
                }
            },
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = Color(0xFF3A3330),
                unfocusedTextColor = Color(0xFF3A3330)
            )
        )

        if (showTimePicker) {
            val timePickerState = rememberTimePickerState(
                initialHour = uiState.selectedTime.hour,
                initialMinute = uiState.selectedTime.minute
            )

            AlertDialog(
                onDismissRequest = { showTimePicker = false },
                confirmButton = {
                    TextButton(onClick = {
                        viewModel.onTimeSelected(
                            java.time.LocalTime.of(timePickerState.hour, timePickerState.minute)
                        )
                        showTimePicker = false
                    }) { Text("OK") }
                },
                dismissButton = {
                    TextButton(onClick = { showTimePicker = false }) { Text("Cancel") }
                },
                text = { TimePicker(state = timePickerState) }
            )
        }

        Spacer(modifier = Modifier.height(12.dp))


        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    onNavigationEvent(NavigationEvent.OpenMap)
                }
                .padding(vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Icon(
                imageVector = Icons.Default.LocationOn,
                contentDescription = null,
                tint = Color(0xFF615A57)
            )

            Spacer(Modifier.width(12.dp))

            Text(
                text = if (uiState.location.isBlank())
                    "Add location"
                else uiState.location,
                fontSize = 14.sp,
                color = Color(0xFF615A57),
                modifier = Modifier.weight(1f)
            )

            if (uiState.location.isNotBlank()) {
                IconButton(onClick = { viewModel.clearLocation() }) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Remove location",
                        tint = Color.Gray
                    )
                }
            }
        }




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
        var moodExpanded by remember { mutableStateOf(false) }

        // Mood button
        MoodDropdown(
            selectedMood = uiState.mood,
            onMoodSelected = { viewModel.onMoodSelected(it) }
        )



        Spacer(modifier = Modifier.height(24.dp))

        // Note
        BasicTextField(
            value = uiState.note,
            onValueChange = viewModel::onNoteChange,
            textStyle = TextStyle(fontSize = 16.sp, lineHeight = 26.sp, color = Color(0xFF3A3330)),
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
            itemsIndexed(uiState.images) { index, img ->
                Box(
                    modifier = Modifier
                        .aspectRatio(1f)
                        .clip(RoundedCornerShape(16.dp))
                        .background(Color(0xFFEDE6DE))
                        .clickable {
                            startIndex = index
                            showViewer = true
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
                                    PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                                )
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

    if (showLocationSheet) {
        ModalBottomSheet(
            onDismissRequest = { showLocationSheet = false }
        ) {
            Text(
                text = "Use Current Location",
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        // later: GPS
                        viewModel.setLocation(
                            latitude = 30.0,
                            longitude = 78.0,
                            name = "Current Location"
                        )
                        showLocationSheet = false
                    }
                    .padding(16.dp)
            )

            Text(
                text = "Pick from Map",
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        // navigate to map screen later
                        showLocationSheet = false
                    }
                    .padding(16.dp)
            )

            Text(
                text = "Remove Location",
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        viewModel.clearLocation()
                        showLocationSheet = false
                    }
                    .padding(16.dp)
            )
        }
    }

    if (showViewer) {
        FullScreenImageViewer(
            images = uiState.images.map { it.localUri },
            startIndex = startIndex,
            onDismiss = { showViewer = false }
        )
    }
}