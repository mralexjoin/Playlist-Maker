package com.akalugin.playlistmaker.domain.playlists

import android.net.Uri
import com.akalugin.playlistmaker.domain.playlists.models.Playlist
import kotlinx.coroutines.flow.Flow

interface PlaylistInteractor {
    suspend fun createPlaylist(playlist: Playlist, imageUri: Uri?)
    fun getPlaylists(): Flow<List<Playlist>>
}