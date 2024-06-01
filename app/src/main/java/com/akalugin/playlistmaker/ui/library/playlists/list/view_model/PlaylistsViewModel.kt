package com.akalugin.playlistmaker.ui.library.playlists.list.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.akalugin.playlistmaker.domain.playlists.PlaylistInteractor
import com.akalugin.playlistmaker.ui.library.playlists.list.models.PlaylistScreenState
import kotlinx.coroutines.launch

class PlaylistsViewModel(
    private val playlistInteractor: PlaylistInteractor,
) : ViewModel() {

    private val _state = MutableLiveData<PlaylistScreenState>(PlaylistScreenState.Loading)
    val state: LiveData<PlaylistScreenState>
        get() = _state

    fun updatePlaylists() {
        _state.postValue(PlaylistScreenState.Loading)
        viewModelScope.launch {
            playlistInteractor.getPlaylists().collect { playlists ->
                _state.postValue(
                    if (playlists.isEmpty()) {
                        PlaylistScreenState.Empty
                    } else {
                        PlaylistScreenState.Content(playlists)
                    }
                )
            }
        }
    }
}