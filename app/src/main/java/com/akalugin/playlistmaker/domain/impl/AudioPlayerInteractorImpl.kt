package com.akalugin.playlistmaker.domain.impl

import com.akalugin.playlistmaker.domain.api.audio_player.AudioPlayerInteractor
import com.akalugin.playlistmaker.domain.api.audio_player.AudioPlayerRepository
import com.akalugin.playlistmaker.domain.models.AudioPlayerState

class AudioPlayerInteractorImpl(override val audioPlayerRepository: AudioPlayerRepository) :
    AudioPlayerInteractor {

    override var onStateChangedListener: AudioPlayerInteractor.OnStateChangedListener? = null
        set(value) {
            field = value
            audioPlayerRepository.onStateChangedListener =
                object : AudioPlayerRepository.OnStateChangedListener {
                    override fun onStateChanged(state: AudioPlayerState) {
                        onStateChangedListener?.onStateChanged(state)
                    }
                }
        }

    override val currentPosition: Int
        get() = audioPlayerRepository.currentPosition

    override fun prepare(dataSource: String) {
        audioPlayerRepository.prepare(dataSource)
    }

    override fun playbackControl() {
        audioPlayerRepository.playbackControl()
    }

    override fun pause() {
        audioPlayerRepository.pause()
    }

    override fun release() {
        audioPlayerRepository.release()
    }
}