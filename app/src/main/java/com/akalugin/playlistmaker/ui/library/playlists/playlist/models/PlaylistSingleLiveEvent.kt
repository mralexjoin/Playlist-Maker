package com.akalugin.playlistmaker.ui.library.playlists.playlist.models

import com.akalugin.playlistmaker.domain.playlists.models.Playlist

sealed interface PlaylistSingleLiveEvent {
    sealed interface ToastEvent : PlaylistSingleLiveEvent {
        data object EmptyPlaylist : ToastEvent
    }

    data object CloseEvent : PlaylistSingleLiveEvent

    data class EditPlaylistEvent(
        val playlist: Playlist?,
    ) : PlaylistSingleLiveEvent
}