package com.akalugin.playlistmaker.ui.player.view_model

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.akalugin.playlistmaker.domain.formatter.Formatter
import com.akalugin.playlistmaker.domain.player.AudioPlayerInteractor
import com.akalugin.playlistmaker.domain.player.models.AudioPlayerState
import com.akalugin.playlistmaker.ui.player.models.AudioPlayerScreenState

class AudioPlayerViewModel(
    private val previewUrl: String,
    private val audioPlayerInteractor: AudioPlayerInteractor,
) : ViewModel() {

    private val mainThreadHandler = Handler(Looper.getMainLooper())
    private val updateCurrentPositionRunnable = Runnable { updateCurrentPosition() }

    private val mAudioPlayerScreenStateLiveData = MutableLiveData(
        if (previewUrl.isEmpty())
            AudioPlayerScreenState.NoPreviewAvailable
        else
            AudioPlayerScreenState.Loading
    )
    val audioPlayerScreenStateLiveData: LiveData<AudioPlayerScreenState>
        get() = mAudioPlayerScreenStateLiveData

    private val currentPosition
        get() = Formatter.formatMilliseconds(
            audioPlayerInteractor.currentPosition,
        )

    init {
        with(audioPlayerInteractor) {
            onStateChangedListener = object : AudioPlayerInteractor.OnStateChangedListener {
                override fun onStateChanged(state: AudioPlayerState) {
                    when (state) {
                        AudioPlayerState.DEFAULT -> {
                            mAudioPlayerScreenStateLiveData.postValue(
                                AudioPlayerScreenState.Loading,
                            )
                        }

                        AudioPlayerState.PREPARED -> {
                            stopUpdateCurrentPosition()
                        }

                        AudioPlayerState.PAUSED -> {
                            stopUpdateCurrentPosition()
                        }

                        AudioPlayerState.PLAYING -> {
                            updateCurrentPosition()
                        }
                    }
                }
            }
            if (previewUrl.isNotEmpty()) {
                prepare(previewUrl)
            }
        }
    }

    fun pause() {
        audioPlayerInteractor.pause()
    }

    fun playbackControl() {
        audioPlayerInteractor.playbackControl()
    }

    fun release() {
        audioPlayerInteractor.release()
        mainThreadHandler.removeCallbacksAndMessages(null)
    }

    private fun updateCurrentPosition() {
        mAudioPlayerScreenStateLiveData.postValue(
            AudioPlayerScreenState.Playing(
                currentPosition,
            )
        )
        mainThreadHandler.postDelayed(
            updateCurrentPositionRunnable,
            UPDATE_PLAYER_ACTIVITY_DELAY_MILLIS
        )
    }

    private fun stopUpdateCurrentPosition() {
        mAudioPlayerScreenStateLiveData.postValue(
            AudioPlayerScreenState.Paused(
                currentPosition,
            )
        )
        mainThreadHandler.removeCallbacks(updateCurrentPositionRunnable)
    }

    companion object {
        private const val UPDATE_PLAYER_ACTIVITY_DELAY_MILLIS = 300L
        fun getViewModelFactory(
            trackUrl: String,
            audioPlayerInteractor: AudioPlayerInteractor,
        ): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                AudioPlayerViewModel(
                    trackUrl,
                    audioPlayerInteractor,
                )
            }
        }
    }
}