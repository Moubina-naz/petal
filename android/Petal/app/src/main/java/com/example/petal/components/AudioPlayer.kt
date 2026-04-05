package com.example.petal.components

import android.media.MediaPlayer

class AudioPlayer {

    private var player: MediaPlayer? = null

    fun play(filePath: String) {
        player = MediaPlayer().apply {
            setDataSource(filePath)
            prepare()
            start()
        }
    }

    fun stop() {
        player?.release()
        player = null
    }
}