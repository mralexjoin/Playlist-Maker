package com.akalugin.playlistmaker.ui.library.playlists.playlist.models

sealed interface PlaylistSingleLiveEvent {
    sealed interface ToastEvent : PlaylistSingleLiveEvent {
        data object EmptyPlaylist : ToastEvent
    }

    data object CloseEvent : PlaylistSingleLiveEvent

}