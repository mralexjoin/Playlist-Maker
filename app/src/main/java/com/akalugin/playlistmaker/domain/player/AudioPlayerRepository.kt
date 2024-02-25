package com.akalugin.playlistmaker.domain.player

import com.akalugin.playlistmaker.domain.player.models.AudioPlayerState

interface AudioPlayerRepository {
    var onStateChangedListener: OnStateChangedListener?
    val currentPosition: Int

    fun prepare(dataSource: String)
    fun playbackControl()
    fun pause()
    fun release()

    interface OnStateChangedListener {
        fun onStateChanged(state: AudioPlayerState)
    }
}