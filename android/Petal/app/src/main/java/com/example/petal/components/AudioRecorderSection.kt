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
import androidx.compose.material3.MaterialTheme
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
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(24.dp)
                    )
                }
                Text(
                    text = "Add voice note",
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.outline
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
            .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(24.dp))
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

    val audioPlayer = remember {
        AudioPlayer().also { player ->
            player.onProgressUpdate = { current, total ->
                currentMs = current
                totalMs = total
            }
            player.onCompletion = {
                isPlaying = false
                currentMs = 0
            }
        }
    }

    // Single DisposableEffect — just stop the player on dispose, no MediaPlayer.release() called directly
    DisposableEffect(Unit) {
        onDispose {
            audioPlayer.stop()
        }
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Default.Mic,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.secondary,
            modifier = Modifier.size(24.dp)
        )
        Spacer(Modifier.width(8.dp))

        IconButton(
            onClick = {
                if (isPlaying) {
                    audioPlayer.pause()
                    isPlaying = false
                } else {
                    if (currentMs > 0) {
                        audioPlayer.resume()
                    } else {
                        audioPlayer.play(file.absolutePath)
                    }
                    isPlaying = true
                }
            },
            modifier = Modifier.size(32.dp)
        ) {
            Icon(
                imageVector = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                contentDescription = "Play",
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(20.dp)
            )
        }

        Spacer(Modifier.width(4.dp))

        Slider(
            value = if (totalMs > 0) currentMs.toFloat() / totalMs else 0f,
            onValueChange = { fraction ->
                val seekTo = (fraction * totalMs).toInt()
                audioPlayer.seekTo(seekTo)
                currentMs = seekTo
            },
            modifier = Modifier.weight(1f),
            thumb = {
                Box(
                    modifier = Modifier
                        .size(12.dp)
                        .background(MaterialTheme.colorScheme.secondary, CircleShape)
                )
            },
            track = { sliderState ->
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(3.dp)
                        .background(MaterialTheme.colorScheme.primary, RoundedCornerShape(2.dp))
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(sliderState.value)
                            .height(3.dp)
                            .background(MaterialTheme.colorScheme.primary, RoundedCornerShape(2.dp))
                    )
                }
            }
        )

        Spacer(Modifier.width(6.dp))

        Text(
            text = formatAudioMs(if (totalMs > 0) totalMs - currentMs else 0),
            color = MaterialTheme.colorScheme.onSurface,
            fontSize = 11.sp
        )

        Spacer(Modifier.width(4.dp))

        IconButton(
            onClick = {
                audioPlayer.stop()
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
                tint = MaterialTheme.colorScheme.primary,
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

    val audioPlayer = remember {
        AudioPlayer().also { player ->
            player.onProgressUpdate = { current, total ->
                currentMs = current
                totalMs = total
            }
            player.onCompletion = {
                isPlaying = false
                currentMs = 0
            }
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            audioPlayer.stop()
        }
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Default.Mic,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.size(18.dp)
        )
        Spacer(Modifier.width(8.dp))

        IconButton(
            onClick = {
                if (isPlaying) {
                    audioPlayer.pause()
                    isPlaying = false
                } else {
                    if (currentMs > 0) {
                        audioPlayer.resume()
                    } else {
                        audioPlayer.playUrl(url)
                    }
                    isPlaying = true
                }
            },
            modifier = Modifier.size(32.dp)
        ) {
            Icon(
                imageVector = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                contentDescription = "Play",
                tint = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.size(20.dp)
            )
        }

        Spacer(Modifier.width(4.dp))

        Slider(
            value = if (totalMs > 0) currentMs.toFloat() / totalMs else 0f,
            onValueChange = { fraction ->
                val seekTo = (fraction * totalMs).toInt()
                audioPlayer.seekTo(seekTo)
                currentMs = seekTo
            },
            modifier = Modifier.weight(1f),
            thumb = {
                Box(
                    modifier = Modifier
                        .size(12.dp)
                        .background(MaterialTheme.colorScheme.secondary, CircleShape)
                )
            },
            track = { sliderState ->
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(3.dp)
                        .background(MaterialTheme.colorScheme.background, RoundedCornerShape(2.dp))
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(sliderState.value)
                            .height(3.dp)
                            .background(MaterialTheme.colorScheme.secondary, RoundedCornerShape(2.dp))
                    )
                }
            }
        )

        Spacer(Modifier.width(6.dp))

        Text(
            text = formatAudioMs(if (totalMs > 0) totalMs - currentMs else 0),
            color = MaterialTheme.colorScheme.onSurface,
            fontSize = 11.sp
        )
    }
}

private fun formatAudioMs(ms: Int): String {
    val secs = ms / 1000
    return "%d:%02d".format(secs / 60, secs % 60)
}