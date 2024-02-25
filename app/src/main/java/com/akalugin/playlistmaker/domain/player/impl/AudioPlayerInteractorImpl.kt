package com.akalugin.playlistmaker.domain.player.impl

import com.akalugin.playlistmaker.domain.player.AudioPlayerInteractor
import com.akalugin.playlistmaker.domain.player.AudioPlayerRepository
import com.akalugin.playlistmaker.domain.player.models.AudioPlayerState

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