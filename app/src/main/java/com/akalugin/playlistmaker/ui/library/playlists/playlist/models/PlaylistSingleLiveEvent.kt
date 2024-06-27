package com.akalugin.playlistmaker.ui.library.playlists.playlist.models

import com.akalugin.playlistmaker.domain.playlists.models.Playlist

sealed interface PlaylistSingleLiveEvent {
    data object EmptyPlaylist : PlaylistSingleLiveEvent

    data object CloseEvent : PlaylistSingleLiveEvent

    data object HideMenu : PlaylistSingleLiveEvent

    data class EditPlaylistEvent(
        val playlist: Playlist?,
    ) : PlaylistSingleLiveEvent
}