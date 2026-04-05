package com.example.petal.ui.addMemory

import android.Manifest
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PieChartOutline
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Stop
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
import com.example.petal.components.AudioPlayer
import com.example.petal.components.AudioRecorder
import com.example.petal.components.ErrorSnackbar
import com.example.petal.components.FullScreenImageViewer
import com.example.petal.components.MoodDropdown
import com.example.petal.domain.Mood
import java.io.File
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
    val bg = Color(0xFFFf9f8f3)
    val black = Color(0xFF2d2d2d)


    var isRecording by remember { mutableStateOf(false) }
    var recordedFile by remember { mutableStateOf<File?>(null) }
    var isPlaying by remember { mutableStateOf(false) }

    val recorder = remember { AudioRecorder(context) }
    val player = remember { AudioPlayer() }
    var progress by remember { mutableStateOf(0f) }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(bg)
            .verticalScroll(rememberScrollState())
            .statusBarsPadding()
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedButton(
                onClick = { onNavigationEvent(NavigationEvent.GoBack) },
                shape = RoundedCornerShape(20.dp),
                border = BorderStroke(1.dp, black),
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = black
                ),
                contentPadding = PaddingValues(horizontal = 20.dp, vertical = 8.dp)
            ) {
                Text("Cancel", style = MaterialTheme.typography.bodyMedium)
            }

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = { onNavigationEvent(NavigationEvent.SaveMemory) },
                enabled = !uiState.isSaving,
                shape = RoundedCornerShape(20.dp),
                border= BorderStroke(1.dp, black),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFd36b54),
                    contentColor = Color.White,
                    disabledContainerColor = Color(0xFFD6CCC2),
                    disabledContentColor = Color.White
                ),
                contentPadding = PaddingValues(horizontal = 20.dp, vertical = 8.dp)
            ) {
                Text(
                    text = if (uiState.isSaving) "Saving..." else "Save",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }


         Spacer(modifier = Modifier.height(12.dp))

             //date
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { showDatePicker = true }
                .padding(vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.DateRange,
                contentDescription = "Date",
                tint = black,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(10.dp))
            Text(
                text = uiState.selectedDate
                    .format(DateTimeFormatter.ofPattern("MMMM d, yyyy").withLocale(java.util.Locale.US))
                    .uppercase(),
                fontSize = 14.sp,
                letterSpacing = 1.2.sp,
                color = black,
                fontWeight = androidx.compose.ui.text.font.FontWeight.SemiBold
            )
        }

        if (showDatePicker) {
            val initialMillis = uiState.selectedDate
                .atStartOfDay(java.time.ZoneId.systemDefault())
                .toInstant()
                .toEpochMilli()

            val datePickerState = rememberDatePickerState(
                initialSelectedDateMillis = initialMillis
            )
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

        Spacer(Modifier.height(2.dp))

        // time
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { showTimePicker = true }
                .padding(vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Schedule,
                contentDescription = "Time",
                tint = black,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(10.dp))
            Text(
                text = uiState.selectedTime
                    .format(DateTimeFormatter.ofPattern("hh:mm a"))
                    .uppercase(),
                fontSize = 14.sp,
                letterSpacing = 1.2.sp,
                color = black,
                fontWeight = androidx.compose.ui.text.font.FontWeight.SemiBold
            )
        }

        if (showTimePicker) {
            val timePickerState = TimePickerState(
                initialHour = uiState.selectedTime.hour,
                initialMinute = uiState.selectedTime.minute,
                is24Hour = false
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

        Spacer(modifier = Modifier.height(2.dp))

        //location
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
                tint = black
            )

            Spacer(Modifier.width(12.dp))

            Text(
                text = uiState.location.ifBlank { "Add location" },
                fontSize = 14.sp,
                color = black,
                fontWeight = androidx.compose.ui.text.font.FontWeight.SemiBold,
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
        Column(modifier = Modifier.fillMaxWidth()) {
            BasicTextField(
                value = uiState.title,
                onValueChange = { if (it.length <= 100) viewModel.onTitleChange(it) },
                textStyle = MaterialTheme.typography.headlineLarge.copy(color = black),
                modifier = Modifier.fillMaxWidth(),
                decorationBox = { inner ->
                    if (uiState.title.isEmpty()) {
                        Text("Add a Title...", style = MaterialTheme.typography.headlineLarge, color = Color(0xFF6b7280))
                    }
                    inner()
                }
            )
            Text(
                "${uiState.title.length}/100",
                fontSize = 11.sp,
                color = Color.Gray,
                modifier = Modifier.align(Alignment.End)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // MOOD

            var moodExpanded by remember { mutableStateOf(false) }
            MoodDropdown(
                selectedMood = uiState.mood,
                onMoodSelected = { viewModel.onMoodSelected(it) }
            )

        Spacer(modifier = Modifier.height(12.dp))

        //Audio recs

        if (recordedFile == null) {

            // 🎤 Mic button
            IconButton(
                onClick = {
                    if (!isRecording) {
                        recorder.start()
                        isRecording = true
                    } else {
                        recordedFile = recorder.stop()
                        isRecording = false
                    }
                }
            ) {
                Icon(
                    imageVector = if (isRecording) Icons.Default.Stop else Icons.Default.Mic,
                    contentDescription = null
                )
            }

        } else {

            // Audio Player
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.Black, RoundedCornerShape(12.dp))
                    .padding(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {

                // 🗑 Delete
                IconButton(onClick = {
                    recorder.delete()
                    recordedFile = null
                    player.stop()
                }) {
                    Icon(Icons.Default.Delete, contentDescription = null, tint = Color.White)
                }

                // ⏱ Duration
                Text("0:03", color = Color.White)

                Spacer(modifier = Modifier.width(8.dp))

                //waveform
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(20.dp)
                        .background(Color.Gray)
                )

                // Play
                IconButton(onClick = {
                    if (!isPlaying) {
                        player.play(recordedFile!!.absolutePath)
                        isPlaying = true
                    } else {
                        player.stop()
                        isPlaying = false
                    }
                }) {
                    Icon(
                        imageVector = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                        contentDescription = null,
                        tint = Color.White
                    )
                }
            }
        }


        Spacer(modifier = Modifier.height(24.dp))

        // Note
        Column(modifier = Modifier.fillMaxWidth()) {
            BasicTextField(
                value = uiState.note,
                onValueChange = { if (it.length <= 10000) viewModel.onNoteChange(it) },
                textStyle = TextStyle(fontSize = 16.sp, lineHeight = 26.sp, color = Color(0xFF3A3330)),
                modifier = Modifier.fillMaxWidth(),
                decorationBox = { inner ->
                    if (uiState.note.isEmpty()) {
                        Text("Start writing...", fontSize = 16.sp, lineHeight = 26.sp, color = Color(0xFFB0AAA3))
                    }
                    inner()
                }
            )
            Text(
                "${uiState.note.length}/10000",
                fontSize = 11.sp,
                color = Color.Gray,
                modifier = Modifier.align(Alignment.End)
            )
        }

        Spacer(modifier = Modifier.height(32.dp))


        uiState.error?.let { error ->
            ErrorSnackbar (
                message   = error,
                onDismiss = { viewModel.clearError() }
            )
        }
        //IMAGES
        Text(
            text = "Captured Moments",
            fontSize = 16.sp,
            letterSpacing = 1.sp,
            color = Color(0xFF9C8F86)
        )

        Spacer(modifier = Modifier.height(12.dp))

        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
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
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        Box(
                            modifier = Modifier
                                .size(52.dp)
                                .clip(RoundedCornerShape(16.dp))
                                .border(1.dp, Color(0xFF2d2d2d), RoundedCornerShape(16.dp))
                                .clickable {
                                    pickerLauncher.launch(
                                        PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                                    )
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                Icons.Default.Add,
                                contentDescription = null,
                                tint = black,
                                modifier = Modifier.size(22.dp)
                            )
                        }
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