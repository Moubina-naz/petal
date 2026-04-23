package com.example.petal.components

import android.media.MediaPlayer
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class AudioPlayer {

    private var player: MediaPlayer? = null
    private var progressJob: Job? = null

    var onProgressUpdate: ((current: Int, total: Int) -> Unit)? = null
    var onCompletion: (() -> Unit)? = null

    fun play(filePath: String) {
        player?.release()
        player = MediaPlayer().apply {
            setDataSource(filePath)
            prepare()
            start()
            setOnCompletionListener {
                onCompletion?.invoke()
                progressJob?.cancel()
            }
        }
        startProgressTracking()
    }

    fun playUrl(url: String) {
        player?.release()
        player = MediaPlayer().apply {
            setDataSource(url)
            prepareAsync()
            setOnPreparedListener { it.start() }
            setOnCompletionListener {
                onCompletion?.invoke()
                progressJob?.cancel()
            }
        }
        startProgressTracking()
    }

    fun pause() {
        player?.pause()
        progressJob?.cancel()
    }

    fun resume() {
        player?.start()
        startProgressTracking()
    }

    fun stop() {
        progressJob?.cancel()
        player?.release()
        player = null
    }

    fun seekTo(millis: Int) {
        player?.seekTo(millis)
    }

    fun getDuration(): Int = player?.duration ?: 0

    private fun startProgressTracking() {
        progressJob?.cancel()
        progressJob = CoroutineScope(Dispatchers.Main + Job()).launch {
            while (true) {
                val p = player ?: break
                try {
                    if (p.isPlaying) {
                        onProgressUpdate?.invoke(p.currentPosition, p.duration)
                    }
                } catch (e: IllegalStateException) {
                    break // player was released mid-loop
                }
                delay(200)
            }
        }
    }
}