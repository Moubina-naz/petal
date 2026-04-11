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
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
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
    onMicClick: () -> Unit,
    onStopClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    when {
        !isRecording && recordedFile == null -> {
            // Clean mic button — no background, just icon
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onMicClick, modifier = Modifier.size(36.dp)) {
                    Icon(
                        imageVector = Icons.Default.Mic,
                        contentDescription = "Record audio",
                        tint = Color(0xFF9C8F86),
                        modifier = Modifier.size(20.dp)
                    )
                }
                Text(
                    text = "Add voice note",
                    fontSize = 14.sp,
                    color = Color(0xFF9C8F86)
                )
            }
        }
        isRecording -> RecordingBar(onStopClick = onStopClick)
        recordedFile != null -> RecordedAudioBar(file = recordedFile, onDeleteClick = onDeleteClick)
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecordedAudioBar(file: File, onDeleteClick: () -> Unit) {
    var isPlaying by remember { mutableStateOf(false) }
    var currentMs by remember { mutableStateOf(0) }
    var totalMs by remember { mutableStateOf(0) }
    val mediaPlayer = remember { MediaPlayer() }

    LaunchedEffect(isPlaying) {
        while (isPlaying) {
            currentMs = mediaPlayer.currentPosition
            if (totalMs == 0 && mediaPlayer.duration > 0) totalMs = mediaPlayer.duration
            delay(200)
        }
    }

    DisposableEffect(Unit) {
        onDispose { mediaPlayer.release() }
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Mic icon (static, just decorative)
        Icon(
            imageVector = Icons.Default.Mic,
            contentDescription = null,
            tint = Color(0xFF2d2d2d),
            modifier = Modifier.size(18.dp)
        )

        Spacer(Modifier.width(8.dp))

        // Play/Pause button
        IconButton(
            onClick = {
                if (isPlaying) {
                    mediaPlayer.pause()
                    isPlaying = false
                } else {
                    mediaPlayer.reset()
                    mediaPlayer.setDataSource(file.absolutePath)
                    mediaPlayer.prepare()
                    if (currentMs > 0) mediaPlayer.seekTo(currentMs)
                    mediaPlayer.start()
                    isPlaying = true
                    mediaPlayer.setOnCompletionListener {
                        isPlaying = false
                        currentMs = 0
                    }
                }
            },
            modifier = Modifier.size(32.dp)
        ) {
            Icon(
                imageVector = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                contentDescription = "Play",
                tint = Color(0xFF2d2d2d),
                modifier = Modifier.size(20.dp)
            )
        }

        Spacer(Modifier.width(4.dp))

        // Slider — green active track
        Slider(
            value = if (totalMs > 0) currentMs.toFloat() / totalMs else 0f,
            onValueChange = { fraction ->
                val seekTo = (fraction * totalMs).toInt()
                mediaPlayer.seekTo(seekTo)
                currentMs = seekTo
            },
            modifier = Modifier.weight(1f),
            thumb = {
                Box(
                    modifier = Modifier
                        .size(12.dp)
                        .background(Color(0xFF6BAF7A), CircleShape)
                )
            },
            track = { sliderState ->
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(3.dp)
                        .background(Color(0xFFD6CCC2), RoundedCornerShape(2.dp))
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(sliderState.value)
                            .height(3.dp)
                            .background(Color(0xFF6BAF7A), RoundedCornerShape(2.dp))
                    )
                }
            }
        )

        Spacer(Modifier.width(6.dp))

        // Time remaining
        Text(
            text = formatAudioMs(if (totalMs > 0) totalMs - currentMs else 0),
            color = Color(0xFF9C8F86),
            fontSize = 11.sp
        )

        Spacer(Modifier.width(4.dp))

        // Delete — small, subtle
        IconButton(
            onClick = {
                mediaPlayer.reset()
                isPlaying = false
                currentMs = 0
                totalMs = 0
                onDeleteClick()
            },
            modifier = Modifier.size(28.dp)
        ) {
            Icon(
                Icons.Default.Delete,
                contentDescription = "Delete",
                tint = Color(0xFFCC6666),
                modifier = Modifier.size(16.dp)
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UrlAudioBar(url: String) {
    var isPlaying by remember { mutableStateOf(false) }
    var currentMs by remember { mutableStateOf(0) }
    var totalMs by remember { mutableStateOf(0) }
    val mediaPlayer = remember { MediaPlayer() }
    var isPrepared by remember { mutableStateOf(false) }

    LaunchedEffect(isPlaying) {
        while (isPlaying) {
            currentMs = mediaPlayer.currentPosition
            if (totalMs == 0 && mediaPlayer.duration > 0) totalMs = mediaPlayer.duration
            delay(200)
        }
    }

    DisposableEffect(Unit) {
        onDispose { mediaPlayer.release() }
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Mic icon (decorative)
        Icon(
            imageVector = Icons.Default.Mic,
            contentDescription = null,
            tint = Color(0xFF2d2d2d),
            modifier = Modifier.size(18.dp)
        )

        Spacer(Modifier.width(8.dp))

        // Play/Pause
        IconButton(
            onClick = {
                if (isPlaying) {
                    mediaPlayer.pause()
                    isPlaying = false
                } else {
                    if (!isPrepared) {
                        mediaPlayer.setDataSource(url)
                        mediaPlayer.prepareAsync()
                        mediaPlayer.setOnPreparedListener {
                            isPrepared = true
                            totalMs = it.duration
                            it.start()
                            isPlaying = true
                        }
                    } else {
                        if (currentMs > 0) mediaPlayer.seekTo(currentMs)
                        mediaPlayer.start()
                        isPlaying = true
                    }
                    mediaPlayer.setOnCompletionListener {
                        isPlaying = false
                        currentMs = 0
                    }
                }
            },
            modifier = Modifier.size(32.dp)
        ) {
            Icon(
                imageVector = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                contentDescription = "Play",
                tint = Color(0xFF2d2d2d),
                modifier = Modifier.size(20.dp)
            )
        }

        Spacer(Modifier.width(4.dp))

        // Slider — green
        Slider(
            value = if (totalMs > 0) currentMs.toFloat() / totalMs else 0f,
            onValueChange = { fraction ->
                val seekTo = (fraction * totalMs).toInt()
                mediaPlayer.seekTo(seekTo)
                currentMs = seekTo
            },
            modifier = Modifier.weight(1f),
            thumb = {
                Box(
                    modifier = Modifier
                        .size(12.dp)
                        .background(Color(0xFF6BAF7A), CircleShape)
                )
            },
            track = { sliderState ->
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(3.dp)
                        .background(Color(0xFFD6CCC2), RoundedCornerShape(2.dp))
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(sliderState.value)
                            .height(3.dp)
                            .background(Color(0xFF6BAF7A), RoundedCornerShape(2.dp))
                    )
                }
            }
        )

        Spacer(Modifier.width(6.dp))

        // Time
        Text(
            text = formatAudioMs(if (totalMs > 0) totalMs - currentMs else 0),
            color = Color(0xFF9C8F86),
            fontSize = 11.sp
        )
    }
}
private fun formatAudioMs(ms: Int): String {
    val secs = ms / 1000
    return "%d:%02d".format(secs / 60, secs % 60)
}