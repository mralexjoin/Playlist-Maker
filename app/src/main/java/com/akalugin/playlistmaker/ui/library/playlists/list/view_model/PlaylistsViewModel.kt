package com.akalugin.playlistmaker.ui.library.playlists.list.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.akalugin.playlistmaker.domain.playlists.PlaylistInteractor
import com.akalugin.playlistmaker.ui.library.playlists.list.models.PlaylistsScreenState
import kotlinx.coroutines.launch

class PlaylistsViewModel(
    private val playlistInteractor: PlaylistInteractor,
) : ViewModel() {

    private val _state = MutableLiveData<PlaylistsScreenState>(PlaylistsScreenState.Loading)
    val state: LiveData<PlaylistsScreenState>
        get() = _state

    fun updatePlaylists() {
        _state.postValue(PlaylistsScreenState.Loading)
        viewModelScope.launch {
            playlistInteractor.getPlaylistsWithTrackCount().collect { playlists ->
                _state.postValue(
                    if (playlists.isEmpty()) {
                        PlaylistsScreenState.Empty
                    } else {
                        PlaylistsScreenState.Content(playlists)
                    }
                )
            }
        }
    }
}