package com.akalugin.playlistmaker.domain.playlists.impl

import android.net.Uri
import com.akalugin.playlistmaker.domain.playlists.PlaylistInteractor
import com.akalugin.playlistmaker.domain.playlists.PlaylistRepository
import com.akalugin.playlistmaker.domain.playlists.models.Playlist
import com.akalugin.playlistmaker.domain.track.models.Track

class PlaylistInteractorImpl(
    private val playlistRepository: PlaylistRepository,
) : PlaylistInteractor {
    override suspend fun createPlaylist(playlist: Playlist, imageUri: Uri?) {
        playlistRepository.add(playlist, imageUri)
    }

    override fun getPlaylistsWithTrackCount() = playlistRepository.getPlaylistsWithTrackCount()

    override fun getPlaylistsWithTracks() = playlistRepository.getPlaylistsWithTracks()

    override suspend fun addTrackToPlaylist(track: Track, playlist: Playlist) {
        playlistRepository.addTrackToPlaylist(track, playlist)
    }
}