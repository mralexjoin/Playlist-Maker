package com.akalugin.playlistmaker.ui.player.models

import com.akalugin.playlistmaker.domain.playlists.models.Playlist

sealed interface BottomSheetPlaylistsState {
    data object Loading : BottomSheetPlaylistsState

    data class Content(
        val playlists: List<Playlist>,
    ) : BottomSheetPlaylistsState
}