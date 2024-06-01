package com.akalugin.playlistmaker.domain.playlists

import com.akalugin.playlistmaker.domain.playlists.models.Playlist

interface PlaylistRepository {
    suspend fun add(playlist: Playlist)
}