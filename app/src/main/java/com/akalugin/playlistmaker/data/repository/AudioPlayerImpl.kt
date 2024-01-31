package com.akalugin.playlistmaker.data.repository

import android.media.MediaPlayer
import com.akalugin.playlistmaker.domain.api.AudioPlayer
import com.akalugin.playlistmaker.domain.api.AudioPlayer.State

class AudioPlayerImpl : AudioPlayer {
    private val mediaPlayer = MediaPlayer()

    private var state = State.DEFAULT
        set(value) {
            field = value
            onStateChangedListener?.onStateChanged(state)
        }

    override var onStateChangedListener: AudioPlayer.OnStateChangedListener? = null

    override val currentPosition: Int
        get() = mediaPlayer.currentPosition

    override fun prepare(dataSource: String) {
        mediaPlayer.setDataSource(dataSource)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            state = State.PREPARED
        }
        mediaPlayer.setOnCompletionListener {
            state = State.PREPARED
        }
    }

    override fun pause() {
        mediaPlayer.pause()
        state = State.PAUSED
    }

    override fun playbackControl() {
        when (state) {
            State.PLAYING -> pause()
            State.PREPARED, State.PAUSED -> start()
            State.DEFAULT -> throw IllegalStateException("AudioPlayer must be prepared before start")
        }
    }

    override fun release() {
        mediaPlayer.release()
    }

    private fun start() {
        mediaPlayer.start()
        state = State.PLAYING
    }

}