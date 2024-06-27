package com.akalugin.playlistmaker.domain.playlists

import com.akalugin.playlistmaker.domain.playlists.models.Playlist
import com.akalugin.playlistmaker.domain.track.models.Track
import kotlinx.coroutines.flow.Flow

interface PlaylistInteractor {
    suspend fun createPlaylist(playlist: Playlist)
    suspend fun updatePlaylist(playlist: Playlist)

    fun getPlaylistsWithTrackCount(): Flow<List<Playlist>>

    fun getPlaylistsWithTracks(): Flow<List<Playlist>>

    suspend fun addTrackToPlaylist(track: Track, playlist: Playlist)

    fun getPlaylistWithTracks(playlistId: Int): Flow<Playlist?>

    suspend fun deleteTrackFromPlaylist(track: Track, playlist: Playlist)
    suspend fun deletePlaylist(playlist: Playlist)
}