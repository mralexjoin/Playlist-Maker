package com.akalugin.playlistmaker.domain.api.audio_player

import com.akalugin.playlistmaker.domain.models.AudioPlayerState

interface AudioPlayerInteractor {
    val audioPlayerRepository: AudioPlayerRepository

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