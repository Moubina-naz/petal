package com.example.petal.components

import android.media.MediaPlayer
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.petal.R
import kotlinx.coroutines.delay
import java.io.File

@Composable
fun AudioRecorderSection(
    isRecording: Boolean,
    recordedFile: File?,
    onMicClick: () -> Unit,      // starts recording
    onStopClick: () -> Unit,     // stops recording
    onDeleteClick: () -> Unit    // deletes the recording
) {
    when {
        // STATE 1: Nothing recorded yet → show mic button
        !isRecording && recordedFile == null -> {
            IconButton(onClick = onMicClick) {
                Icon(
                    painter = painterResource(R.drawable.content), // standard mic icon
                    contentDescription = "Record audio"
                )
            }
        }

        // STATE 2: Currently recording → show stop button + timer
        isRecording -> {
            RecordingBar(onStopClick = onStopClick)
        }

        // STATE 3: Recording done → show playback bar with delete
        recordedFile != null -> {
            RecordedAudioBar(
                file = recordedFile,
                onDeleteClick = onDeleteClick
            )
        }
    }
}

@Composable
fun RecordingBar(onStopClick: () -> Unit) {
    var seconds by remember { mutableStateOf(0) }

    // Timer
    LaunchedEffect(Unit) {
        while (true) {
            delay(1000)
            seconds++
        }
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .background(Color(0xFFF5F5F5), RoundedCornerShape(24.dp))
            .padding(horizontal = 16.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Red dot
        Box(
            modifier = Modifier
                .size(10.dp)
                .background(Color.Red, CircleShape)
        )
        Spacer(Modifier.width(8.dp))

        // Timer text
        Text(
            text = "%d:%02d".format(seconds / 60, seconds % 60),
            fontSize = 14.sp
        )
        Spacer(Modifier.weight(1f))

        // Stop button
        IconButton(onClick = onStopClick) {
            Icon(Icons.Default.Stop, contentDescription = "Stop")
        }
    }
}

@Composable
fun RecordedAudioBar(file: File, onDeleteClick: () -> Unit) {
    val context = LocalContext.current
    var isPlaying by remember { mutableStateOf(false) }
    val mediaPlayer = remember { MediaPlayer() }

    DisposableEffect(Unit) {
        onDispose { mediaPlayer.release() }
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .background(Color(0xFFF5F5F5), RoundedCornerShape(24.dp))
            .padding(horizontal = 16.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Delete button
        IconButton(onClick = onDeleteClick) {
            Icon(Icons.Default.Delete, contentDescription = "Delete", tint = Color.Red)
        }

        // Play/Pause
        IconButton(onClick = {
            if (isPlaying) {
                mediaPlayer.pause()
                isPlaying = false
            } else {
                mediaPlayer.reset()
                mediaPlayer.setDataSource(file.absolutePath)
                mediaPlayer.prepare()
                mediaPlayer.start()
                isPlaying = true
                mediaPlayer.setOnCompletionListener { isPlaying = false }
            }
        }) {
            Icon(
                if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                contentDescription = "Play"
            )
        }

        // Simple line (the "waveform" placeholder)
        Box(
            modifier = Modifier
                .weight(1f)
                .height(2.dp)
                .background(Color.Gray)
        )
    }
}