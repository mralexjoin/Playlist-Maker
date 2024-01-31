package com.akalugin.playlistmaker.domain.api

interface AudioPlayer {
    var onStateChangedListener: OnStateChangedListener?
    val currentPosition: Int

    fun prepare(dataSource: String)
    fun playbackControl()
    fun pause()
    fun release()
    enum class State {
        DEFAULT,
        PREPARED,
        PLAYING,
        PAUSED
    }

    interface OnStateChangedListener {
        fun onStateChanged(state: State)
    }
}