package com.akalugin.playlistmaker.domain.playlists

import android.net.Uri
import com.akalugin.playlistmaker.domain.playlists.models.Playlist
import kotlinx.coroutines.flow.Flow

interface PlaylistRepository {
    suspend fun add(playlist: Playlist, imageUri: Uri?)
    fun getPlaylistsWithTrackCount(): Flow<List<Playlist>>
}