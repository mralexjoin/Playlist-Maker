package com.akalugin.playlistmaker.domain.playlists

import com.akalugin.playlistmaker.domain.playlists.models.Playlist
import com.akalugin.playlistmaker.domain.track.models.Track
import kotlinx.coroutines.flow.Flow

interface PlaylistRepository {
    suspend fun add(playlist: Playlist)
    suspend fun update(playlist: Playlist)

    fun getPlaylistsWithTrackCount(): Flow<List<Playlist>>

    fun getPlaylistsWithTracks(): Flow<List<Playlist>>

    suspend fun addTrackToPlaylist(track: Track, playlist: Playlist)

    fun getPlaylistWithTracks(playlistId: Int): Flow<Playlist?>

    suspend fun deleteTrackFromPlaylist(trackId: Int, playlistId: Int)
    suspend fun deletePlaylist(playlist: Playlist)
}