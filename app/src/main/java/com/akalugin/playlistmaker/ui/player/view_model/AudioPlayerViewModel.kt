package com.akalugin.playlistmaker.ui.player.view_model

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.akalugin.playlistmaker.R
import com.akalugin.playlistmaker.domain.formatter.Formatter
import com.akalugin.playlistmaker.domain.player.AudioPlayerInteractor
import com.akalugin.playlistmaker.domain.player.models.AudioPlayerState

class AudioPlayerViewModel(
    private val previewUrl: String,
    private val audioPlayerInteractor: AudioPlayerInteractor,
) : ViewModel() {

    private val mainThreadHandler = Handler(Looper.getMainLooper())
    private val updateCurrentPositionRunnable = Runnable { updateCurrentPosition() }

    private val mCurrentPositionLiveData = MutableLiveData<String>()
    val currentPositionLiveData: LiveData<String>
        get() = mCurrentPositionLiveData

    private val mPlaybackButtonEnabledLiveData = MutableLiveData(false)
    val playbackButtonEnabledLiveData: LiveData<Boolean>
        get() = mPlaybackButtonEnabledLiveData

    private val mPlaybackButtonDrawableLiveData = MutableLiveData(R.drawable.play)
    val playbackButtonDrawableLiveData: LiveData<Int>
        get() = mPlaybackButtonDrawableLiveData

    init {
        with(audioPlayerInteractor) {
            onStateChangedListener = object : AudioPlayerInteractor.OnStateChangedListener {
                override fun onStateChanged(state: AudioPlayerState) {
                    if (state == AudioPlayerState.PLAYING) {
                        updateCurrentPosition()
                        mPlaybackButtonDrawableLiveData.postValue(R.drawable.pause)
                    } else {
                        stopUpdateCurrentPosition()
                        mPlaybackButtonDrawableLiveData.postValue(R.drawable.play)

                        if (state == AudioPlayerState.PREPARED) {
                            mPlaybackButtonEnabledLiveData.postValue(true)
                            setCurrentPosition(0)
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
        setCurrentPosition(
            audioPlayerInteractor.currentPosition + UPDATE_PLAYER_ACTIVITY_DELAY_MILLIS
        )
        mainThreadHandler.postDelayed(
            updateCurrentPositionRunnable,
            UPDATE_PLAYER_ACTIVITY_DELAY_MILLIS.toLong()
        )
    }

    private fun setCurrentPosition(currentPositionInMilliseconds: Int) =
        mCurrentPositionLiveData.postValue(
            Formatter.formatMilliseconds(
                currentPositionInMilliseconds
            )
        )

    private fun stopUpdateCurrentPosition() {
        mainThreadHandler.removeCallbacks(updateCurrentPositionRunnable)
    }

    companion object {
        private const val UPDATE_PLAYER_ACTIVITY_DELAY_MILLIS = 300
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