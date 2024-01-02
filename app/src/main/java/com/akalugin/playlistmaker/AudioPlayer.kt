package com.akalugin.playlistmaker

import android.media.MediaPlayer

class AudioPlayer {
    private val mediaPlayer = MediaPlayer()
    private var state = State.DEFAULT
        set(value) {
            field = value
            onStateChangedListener?.onStateChanged(state)
        }

    var onStateChangedListener: OnStateChangedListener? = null
    val currentPosition
        get() = mediaPlayer.currentPosition

    fun prepare(dataSource: String) {
        mediaPlayer.setDataSource(dataSource)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            state = State.PREPARED
        }
        mediaPlayer.setOnCompletionListener {
            state = State.PREPARED
        }
    }

    private fun start() {
        mediaPlayer.start()
        state = State.PLAYING
    }

    fun pause() {
        mediaPlayer.pause()
        state = State.PAUSED
    }

    fun playbackControl() {
        when (state) {
            State.PLAYING -> pause()
            State.PREPARED, State.PAUSED -> start()
            State.DEFAULT -> throw IllegalStateException("AudioPlayer must be prepared before start")
        }
    }

    fun release() {
        mediaPlayer.release()
    }

    fun interface OnStateChangedListener {
        fun onStateChanged(state: State)
    }

    enum class State {
        DEFAULT,
        PREPARED,
        PLAYING,
        PAUSED
    }
}