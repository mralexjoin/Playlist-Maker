package com.akalugin.playlistmaker.ui.library.playlists.playlist.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.akalugin.playlistmaker.domain.playlists.PlaylistInteractor
import com.akalugin.playlistmaker.domain.playlists.models.Playlist
import com.akalugin.playlistmaker.domain.sharing.SharingInteractor
import com.akalugin.playlistmaker.domain.track.models.Track
import com.akalugin.playlistmaker.ui.library.playlists.playlist.models.PlaylistScreenState
import com.akalugin.playlistmaker.ui.library.playlists.playlist.models.PlaylistSingleLiveEvent
import com.akalugin.playlistmaker.ui.utils.SingleLiveEvent
import kotlinx.coroutines.launch

class PlaylistViewModel(
    private val playlistInteractor: PlaylistInteractor,
    private val sharingInteractor: SharingInteractor,
) : ViewModel() {

    var playlist: Playlist? = null

    private val _state = MutableLiveData<PlaylistScreenState>()
    val state: LiveData<PlaylistScreenState>
        get() = _state

    private val _singleLiveEvent = SingleLiveEvent<PlaylistSingleLiveEvent>()
    val singleLiveEvent: LiveData<PlaylistSingleLiveEvent>
        get() = _singleLiveEvent


    fun loadPlaylist(playlistId: Int?) {
        if (playlistId == null)
            return

        viewModelScope.launch {
            updatePlaylist(playlistId)
        }
    }

    fun deleteTrack(track: Track) {
        playlist?.let { currentPlaylist ->
            viewModelScope.launch {
                playlistInteractor.deleteTrackFromPlaylist(track, currentPlaylist)
                updatePlaylist(currentPlaylist.playlistId)
            }
        }
    }

    private suspend fun updatePlaylist(playlistId: Int) {
        playlistInteractor.getPlaylistWithTracks(playlistId).collect {
            if (it != null) {
                playlist = it
                _state.postValue(PlaylistScreenState(it))
            }
        }
    }

    fun sharePlaylist() {
        playlist.let { currentPlaylist ->
            if (currentPlaylist == null || currentPlaylist.trackCount == 0)
                _singleLiveEvent.postValue(PlaylistSingleLiveEvent.ToastEvent.EmptyPlaylist)
            else
                sharingInteractor.sharePlaylist(currentPlaylist)
        }
    }

    fun deletePlaylist() {
        playlist?.let {
            viewModelScope.launch {
                playlistInteractor.deletePlaylist(it)
                _singleLiveEvent.postValue(PlaylistSingleLiveEvent.CloseEvent)
            }
        }
    }
}