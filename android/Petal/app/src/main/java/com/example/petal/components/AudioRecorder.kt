package com.example.petal.components

import android.content.Context
import android.media.MediaRecorder
import android.media.MediaRecorder.AudioSource.MIC
import android.media.MediaRecorder.OutputFormat.MPEG_4
import java.io.File

class AudioRecorder(private val context: Context) {

    private var recorder: MediaRecorder? = null
    private var audioFile: File? = null

    fun start(): File {
        val file = File(context.cacheDir, "audio_${System.currentTimeMillis()}.mp4")
        audioFile = file

        recorder = MediaRecorder().apply {
            setAudioSource(MediaRecorder.AudioSource.MIC)
            setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
            setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
            setOutputFile(file.absolutePath)
            prepare()
            start()
        }
        return file
    }

    fun stop(): File? {
        recorder?.stop()
        recorder?.release()
        recorder = null
        return audioFile
    }

    fun delete() {
        audioFile?.delete()
        audioFile = null
    }
}