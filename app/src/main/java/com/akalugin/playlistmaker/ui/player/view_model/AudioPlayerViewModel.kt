package com.akalugin.playlistmaker.ui.player.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.akalugin.playlistmaker.domain.formatter.Formatter
import com.akalugin.playlistmaker.domain.player.AudioPlayerInteractor
import com.akalugin.playlistmaker.domain.player.models.AudioPlayerState
import com.akalugin.playlistmaker.ui.player.models.AudioPlayerScreenState
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class AudioPlayerViewModel(
    private val previewUrl: String,
    private val audioPlayerInteractor: AudioPlayerInteractor,
) : ViewModel() {

    private val mAudioPlayerScreenStateLiveData = MutableLiveData(
        if (previewUrl.isEmpty())
            AudioPlayerScreenState.NoPreviewAvailable
        else
            AudioPlayerScreenState.Loading
    )
    val audioPlayerScreenStateLiveData: LiveData<AudioPlayerScreenState>
        get() = mAudioPlayerScreenStateLiveData

    init {
        with(audioPlayerInteractor) {
            onStateChangedListener = object : AudioPlayerInteractor.OnStateChangedListener {
                private var timerJob: Job? = null

                override fun onStateChanged(state: AudioPlayerState) {
                    when (state) {
                        AudioPlayerState.DEFAULT -> {
                            mAudioPlayerScreenStateLiveData.postValue(
                                AudioPlayerScreenState.Loading
                            )
                        }

                        AudioPlayerState.PREPARED, AudioPlayerState.PAUSED -> {
                            timerJob?.cancel()
                            mAudioPlayerScreenStateLiveData.postValue(
                                AudioPlayerScreenState.Paused(
                                    currentPositionInMillis,
                                )
                            )
                        }

                        AudioPlayerState.PLAYING -> {
                            timerJob = viewModelScope.launch {
                                while (true) {
                                    mAudioPlayerScreenStateLiveData.postValue(
                                        AudioPlayerScreenState.Playing(
                                            currentPositionInMillis,
                                        )
                                    )
                                    delay(UPDATE_PLAYER_ACTIVITY_DELAY_MILLIS)
                                }
                            }
                        }
                    }
                }

                private val currentPositionInMillis
                    get() = Formatter.formatMilliseconds(
                        currentPosition,
                    )
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
    }

    companion object {
        private const val UPDATE_PLAYER_ACTIVITY_DELAY_MILLIS = 300L
    }
}