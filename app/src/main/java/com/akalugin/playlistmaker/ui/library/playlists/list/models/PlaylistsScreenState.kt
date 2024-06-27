package com.akalugin.playlistmaker.ui.library.playlists.list.models

import com.akalugin.playlistmaker.domain.playlists.models.Playlist

sealed interface PlaylistsScreenState {
    data object Loading : PlaylistsScreenState

    data class Content(
        val playlists: List<Playlist>,
    ) : PlaylistsScreenState

    data object Empty : PlaylistsScreenState
}