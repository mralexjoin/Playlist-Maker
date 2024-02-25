package com.akalugin.playlistmaker.data.player.impl

import android.media.MediaPlayer
import com.akalugin.playlistmaker.domain.player.AudioPlayerRepository
import com.akalugin.playlistmaker.domain.player.models.AudioPlayerState

class AudioPlayerRepositoryImpl(private val mediaPlayer: MediaPlayer) : AudioPlayerRepository {
    private var state = AudioPlayerState.DEFAULT
        set(value) {
            field = value
            onStateChangedListener?.onStateChanged(state)
        }

    override var onStateChangedListener: AudioPlayerRepository.OnStateChangedListener? = null

    override val currentPosition: Int
        get() = mediaPlayer.currentPosition

    override fun prepare(dataSource: String) {
        mediaPlayer.setDataSource(dataSource)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            state = AudioPlayerState.PREPARED
        }
        mediaPlayer.setOnCompletionListener {
            state = AudioPlayerState.PREPARED
        }
    }

    override fun pause() {
        mediaPlayer.pause()
        state = AudioPlayerState.PAUSED
    }

    override fun playbackControl() {
        when (state) {
            AudioPlayerState.PLAYING -> pause()
            AudioPlayerState.PREPARED, AudioPlayerState.PAUSED -> start()
            AudioPlayerState.DEFAULT -> throw IllegalStateException("AudioPlayer must be prepared before start")
        }
    }

    override fun release() {
        mediaPlayer.release()
    }

    private fun start() {
        mediaPlayer.start()
        state = AudioPlayerState.PLAYING
    }

}