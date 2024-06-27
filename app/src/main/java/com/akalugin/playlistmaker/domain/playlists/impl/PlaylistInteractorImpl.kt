package com.akalugin.playlistmaker.domain.playlists.impl

import com.akalugin.playlistmaker.domain.playlists.PlaylistInteractor
import com.akalugin.playlistmaker.domain.playlists.PlaylistRepository
import com.akalugin.playlistmaker.domain.playlists.models.Playlist
import com.akalugin.playlistmaker.domain.track.models.Track

class PlaylistInteractorImpl(
    private val playlistRepository: PlaylistRepository,
) : PlaylistInteractor {
    override suspend fun createPlaylist(playlist: Playlist) {
        playlistRepository.add(playlist)
    }

    override suspend fun updatePlaylist(playlist: Playlist) {
        playlistRepository.update(playlist)
    }

    override fun getPlaylistsWithTrackCount() = playlistRepository.getPlaylistsWithTrackCount()

    override fun getPlaylistsWithTracks() = playlistRepository.getPlaylistsWithTracks()

    override suspend fun addTrackToPlaylist(track: Track, playlist: Playlist) {
        playlistRepository.addTrackToPlaylist(track, playlist)
    }

    override fun getPlaylistWithTracks(playlistId: Int) =
        playlistRepository.getPlaylistWithTracks(playlistId)

    override suspend fun deleteTrackFromPlaylist(track: Track, playlist: Playlist) {
        playlistRepository.deleteTrackFromPlaylist(track.trackId, playlist.playlistId)
    }

    override suspend fun deletePlaylist(playlist: Playlist) {
        playlistRepository.deletePlaylist(playlist)
    }
}