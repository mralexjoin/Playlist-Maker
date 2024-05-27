package com.akalugin.playlistmaker.ui.player.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.akalugin.playlistmaker.domain.favorites.FavoriteTracksInteractor
import com.akalugin.playlistmaker.domain.formatter.Formatter
import com.akalugin.playlistmaker.domain.player.AudioPlayerInteractor
import com.akalugin.playlistmaker.domain.player.models.AudioPlayerState
import com.akalugin.playlistmaker.domain.search.models.Track
import com.akalugin.playlistmaker.ui.player.models.AudioPlayerScreenState
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class AudioPlayerViewModel(
    private val track: Track,
    private val audioPlayerInteractor: AudioPlayerInteractor,
    private val favoriteTracksInteractor: FavoriteTracksInteractor,
) : ViewModel() {
    private var screenState =
        AudioPlayerScreenState(
            if (track.previewUrl.isEmpty())
                AudioPlayerScreenState.PlayerState.NO_PREVIEW_AVAILABLE
            else
                AudioPlayerScreenState.PlayerState.LOADING,
            false,
            track.isFavorite,
            ""
        )
        set(value) {
            field = value
            _audioPlayerScreenStateLiveData.postValue(screenState)
        }

    private val _audioPlayerScreenStateLiveData = MutableLiveData(screenState)
    val audioPlayerScreenStateLiveData: LiveData<AudioPlayerScreenState>
        get() = _audioPlayerScreenStateLiveData

    init {
        with(audioPlayerInteractor) {
            onStateChangedListener = object : AudioPlayerInteractor.OnStateChangedListener {
                private var timerJob: Job? = null

                override fun onStateChanged(state: AudioPlayerState) {
                    when (state) {
                        AudioPlayerState.DEFAULT -> {
                            screenState = screenState.copy(
                                playerState = AudioPlayerScreenState.PlayerState.LOADING,
                                isPlaying = false,
                                currentPosition = currentPositionInMillis,
                            )
                        }

                        AudioPlayerState.PREPARED, AudioPlayerState.PAUSED -> {
                            timerJob?.cancel()
                            screenState = screenState.copy(
                                playerState = AudioPlayerScreenState.PlayerState.READY,
                                isPlaying = false,
                                currentPosition = currentPositionInMillis,
                            )
                        }

                        AudioPlayerState.PLAYING -> {
                            timerJob = viewModelScope.launch {
                                while (true) {
                                    screenState = screenState.copy(
                                        isPlaying = true,
                                        currentPosition = currentPositionInMillis
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

            if (track.previewUrl.isNotEmpty()) {
                prepare(track.previewUrl)
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

    fun onFavoriteClicked() {
        viewModelScope.launch {
            if (track.isFavorite) {
                favoriteTracksInteractor.removeFromFavorites(track)
            } else {
                favoriteTracksInteractor.addToFavorites(track)
            }
            track.isFavorite = !track.isFavorite
            screenState = screenState.copy(isFavorite = track.isFavorite)
        }
    }

    companion object {
        private const val UPDATE_PLAYER_ACTIVITY_DELAY_MILLIS = 300L
    }
}