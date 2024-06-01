package com.akalugin.playlistmaker.ui.library.playlists.view_model

import androidx.lifecycle.ViewModel
import com.akalugin.playlistmaker.domain.playlists.PlaylistInteractor

class PlaylistsViewModel(
    private val playlistInteractor: PlaylistInteractor,
) : ViewModel() {
}