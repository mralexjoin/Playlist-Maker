package com.akalugin.playlistmaker.ui.player.view_model

import androidx.annotation.StringRes
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.akalugin.playlistmaker.R
import com.akalugin.playlistmaker.domain.favorites.FavoriteTracksInteractor
import com.akalugin.playlistmaker.domain.formatter.Formatter
import com.akalugin.playlistmaker.domain.player.AudioPlayerInteractor
import com.akalugin.playlistmaker.domain.player.models.AudioPlayerState
import com.akalugin.playlistmaker.domain.playlists.PlaylistInteractor
import com.akalugin.playlistmaker.domain.playlists.models.Playlist
import com.akalugin.playlistmaker.domain.track.models.Track
import com.akalugin.playlistmaker.ui.player.models.AudioPlayerScreenState
import com.akalugin.playlistmaker.ui.player.models.BottomSheetPlaylistsState
import com.akalugin.playlistmaker.ui.utils.SingleLiveEvent
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class AudioPlayerViewModel(
    private val track: Track?,
    private val audioPlayerInteractor: AudioPlayerInteractor,
    private val favoriteTracksInteractor: FavoriteTracksInteractor,
    private val playlistInteractor: PlaylistInteractor,
) : ViewModel() {
    private var screenState =
        AudioPlayerScreenState(
            playerState = if (track == null || track.previewUrl.isEmpty())
                AudioPlayerScreenState.PlayerState.NO_PREVIEW_AVAILABLE
            else
                AudioPlayerScreenState.PlayerState.LOADING,
            isPlaying = false,
            isFavorite = track?.isFavorite ?: false,
            currentPosition = "",
            isBottomSheetVisible = false
        )
        set(value) {
            field = value
            _audioPlayerScreenStateLiveData.postValue(screenState)
        }

    private val _audioPlayerScreenStateLiveData = MutableLiveData(screenState)
    val audioPlayerScreenStateLiveData: LiveData<AudioPlayerScreenState>
        get() = _audioPlayerScreenStateLiveData

    private var playlists: List<Playlist> = emptyList()
    private val _bottomSheetState = MutableLiveData<BottomSheetPlaylistsState>()
    val bottomSheetState: LiveData<BottomSheetPlaylistsState>
        get() = _bottomSheetState

    private val _showToast = SingleLiveEvent<Pair<Int, Array<Any>>>()
    val showToast: LiveData<Pair<Int, Array<Any>>>
        get() = _showToast

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

            track?.let { track ->
                if (track.previewUrl.isNotEmpty()) {
                    prepare(track.previewUrl)
                }
            }
        }
    }

    fun pause() {
        audioPlayerInteractor.pause()
    }

    fun playbackControl() {
        audioPlayerInteractor.playbackControl()
    }

    override fun onCleared() {
        super.onCleared()
        audioPlayerInteractor.release()
    }

    fun onFavoriteClicked() {
        track?.let { track ->
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
    }

    fun onAddToPlaylistButtonClicked() {
        screenState = screenState.copy(isBottomSheetVisible = true)
    }

    fun updatePlaylists() {
        _bottomSheetState.postValue(BottomSheetPlaylistsState.Loading)

        viewModelScope.launch {
            playlistInteractor.getPlaylistsWithTracks().collect {
                playlists = it
                _bottomSheetState.postValue(BottomSheetPlaylistsState.Content(it))
            }
        }
    }

    fun addTrackToPlaylist(track: Track?, playlist: Playlist) {
        if (track == null)
            return

        if (playlist.tracks.contains(track)) {
            showToast(R.string.playlist_contains_track_already, playlist.name)
            return
        }

        viewModelScope.launch {
            playlistInteractor.addTrackToPlaylist(track, playlist)
            addTrackToPlaylistInAdapter(track, playlist)
            screenState = screenState.copy(isBottomSheetVisible = false)
            showToast(R.string.track_added_to_playlist, playlist.name)
        }
    }

    private fun addTrackToPlaylistInAdapter(track: Track, playlist: Playlist) {
        playlists = playlists.toMutableList().apply {
            replaceAll { currentPlayList ->
                if (currentPlayList.playlistId == playlist.playlistId) {
                    val tracks = buildList {
                        addAll(currentPlayList.tracks)
                        add(track)
                    }
                    currentPlayList.copy(
                        tracks = tracks,
                        trackCount = tracks.size
                    )
                } else {
                    currentPlayList
                }
            }
        }.toList()
        _bottomSheetState.postValue(BottomSheetPlaylistsState.Content(playlists))
    }

    private fun showToast(@StringRes stringResId: Int, vararg params: Any) {
        _showToast.postValue(stringResId to arrayOf(*params))
    }

    companion object {
        private const val UPDATE_PLAYER_ACTIVITY_DELAY_MILLIS = 300L
    }
}