package com.akalugin.playlistmaker.domain.playlists

import com.akalugin.playlistmaker.domain.playlists.models.Playlist

interface PlaylistInteractor {
    suspend fun addPlaylist(playlist: Playlist)
}