package com.example.petal.ui.editMemory

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TimePicker
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.petal.NavigationEvent
import com.example.petal.domain.MemoryImage
import androidx.compose.ui.text.TextStyle
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import coil.compose.AsyncImage
import com.example.petal.components.ErrorSnackbar
import com.example.petal.components.FullScreenImageViewer
import com.example.petal.components.MoodDropdown
import java.time.Instant
import java.time.LocalTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditMemoryScreen(
    viewModel: EditMemoryViewModel,
    onNavigationEvent: (NavigationEvent) -> Unit = {},
) {
    var isFavorite by rememberSaveable { mutableStateOf(false) }
    var showMenu by remember { mutableStateOf(false) }

    val uiState by viewModel.uiState.collectAsState()
    val pickerLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.PickMultipleVisualMedia(maxItems = 5)
    ) { uris ->
        uris.forEach { uri ->
            viewModel.addImage(uri.toString())
        }
    }

    var showViewer by remember { mutableStateOf(false) }
    var startIndex by remember { mutableStateOf(0) }
    var showDatePicker by remember { mutableStateOf(false) }
    var showTimePicker by remember { mutableStateOf(false) }

    val navigator = LocalNavigator.currentOrThrow
    val bg = Color(0xFFFf9f8f3)
    val black = Color(0xFF2d2d2d)

    Box(modifier = Modifier
        .fillMaxSize())
    {
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
                    border = BorderStroke(1.dp, black),
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
            Spacer(modifier = Modifier.height(32.dp))


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
                        .format(
                            DateTimeFormatter.ofPattern("MMMM d, yyyy")
                                .withLocale(java.util.Locale.US)
                        )
                        .uppercase(),
                    fontSize = 14.sp,
                    letterSpacing = 1.2.sp,
                    color = black,
                    fontWeight = androidx.compose.ui.text.font.FontWeight.SemiBold
                )
            }

            if (showDatePicker) {
                val initialMillis = uiState.selectedDate
                    .atStartOfDay(ZoneId.systemDefault())
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
                                val selected = Instant.ofEpochMilli(millis)
                                    .atZone(ZoneId.systemDefault())
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

            Spacer(modifier = Modifier.height(2.dp))

            // Time field (clickable, read-only)
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
                val timePickerState = rememberTimePickerState(
                    initialHour = uiState.selectedTime.hour,
                    initialMinute = uiState.selectedTime.minute,
                    is24Hour = false
                )

                AlertDialog(
                    onDismissRequest = { showTimePicker = false },
                    confirmButton = {
                        TextButton(onClick = {
                            viewModel.onTimeSelected(
                                LocalTime.of(timePickerState.hour, timePickerState.minute)
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

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        onNavigationEvent(NavigationEvent.OpenMap)
                    }
                    .padding(vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.LocationOn,
                    contentDescription = null,
                    tint = black
                )

                Spacer(Modifier.width(12.dp))

                Text(
                    text = uiState.locationName.ifBlank { "Add location" },
                    fontSize = 14.sp,
                    color = black,
                    fontWeight = androidx.compose.ui.text.font.FontWeight.SemiBold,
                    modifier = Modifier.weight(1f)
                )

                if (uiState.locationName.isNotBlank()) {
                    IconButton(onClick = {
                        viewModel.onLocationNameChange("")
                    }) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Clear location",
                            tint = Color.Gray
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.width(24.dp))

            TextField(
                value = uiState.title,
                onValueChange = { viewModel.onTitleChange(it) },
                textStyle = MaterialTheme.typography.headlineLarge,
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                ),
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))

            //MOOD
            MoodDropdown(
                selectedMood = uiState.mood,
                onMoodSelected = { viewModel.onMoodSelected(it) }
            )

            Spacer(modifier = Modifier.height(24.dp))

            TextField(
                value = uiState.note,
                onValueChange = { viewModel.onNoteChange(it) },
                modifier = Modifier.fillMaxWidth(),
                textStyle = TextStyle(
                    fontSize = 16.sp,
                    lineHeight = 26.sp
                ),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                )
            )


            Spacer(modifier = Modifier.height(32.dp))

            Text(
                text = "Captured Moments",
                fontSize = 16.sp,
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
                    ) {
                        AsyncImage(
                            model = img.localUri,
                            contentDescription = null,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .fillMaxSize()
                                .clickable {
                                    startIndex = index
                                    showViewer = true
                                }
                        )


                        IconButton(
                            onClick = {
                                onNavigationEvent(
                                    NavigationEvent.RemoveImage(img.localUri)
                                )
                            },
                            modifier = Modifier
                                .align(Alignment.TopEnd)
                                .padding(6.dp)
                                .size(24.dp)
                                .background(Color.White, RoundedCornerShape(50))
                        ) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Remove",
                                modifier = Modifier.size(14.dp)
                            )
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
        val errorMessage by viewModel.errorMessage.collectAsState(initial = null)
        errorMessage?.let {
            ErrorSnackbar(message = it, onDismiss = { viewModel.clearError() })
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

@Preview
@Composable
fun EditMemoryScreenPreview(){
   // EditMemoryScreen()
}