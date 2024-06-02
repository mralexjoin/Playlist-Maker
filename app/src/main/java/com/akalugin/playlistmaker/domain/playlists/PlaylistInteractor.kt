package com.akalugin.playlistmaker.domain.playlists

import android.net.Uri
import com.akalugin.playlistmaker.domain.playlists.models.Playlist
import com.akalugin.playlistmaker.domain.track.models.Track
import kotlinx.coroutines.flow.Flow

interface PlaylistInteractor {
    suspend fun createPlaylist(playlist: Playlist, imageUri: Uri?)
    fun getPlaylistsWithTrackCount(): Flow<List<Playlist>>
    fun getPlaylistsWithTracks(): Flow<List<Playlist>>
    suspend fun addTrackToPlaylist(track: Track, playlist: Playlist)
}