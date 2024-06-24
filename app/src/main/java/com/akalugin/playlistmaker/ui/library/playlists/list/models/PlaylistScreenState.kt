package com.akalugin.playlistmaker.ui.library.playlists.list.models

import com.akalugin.playlistmaker.domain.playlists.models.Playlist

sealed interface PlaylistScreenState {
    data object Loading : PlaylistScreenState

    data class Content(
        val playlists: List<Playlist>,
    ) : PlaylistScreenState

    data object Empty : PlaylistScreenState
}