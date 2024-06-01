package com.akalugin.playlistmaker.domain.playlists.impl

import com.akalugin.playlistmaker.domain.playlists.PlaylistInteractor
import com.akalugin.playlistmaker.domain.playlists.PlaylistRepository
import com.akalugin.playlistmaker.domain.playlists.models.Playlist

class PlaylistInteractorImpl(
    private val playlistRepository: PlaylistRepository,
) : PlaylistInteractor {
    override suspend fun addPlaylist(playlist: Playlist) {
        playlistRepository.add(playlist)
    }
}