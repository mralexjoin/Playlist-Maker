package com.akalugin.playlistmaker.data.playlists.impl

import android.net.Uri
import com.akalugin.playlistmaker.data.db.AppDatabase
import com.akalugin.playlistmaker.data.db.playlists.mapper.PlaylistMapper
import com.akalugin.playlistmaker.domain.files.FileRepository
import com.akalugin.playlistmaker.domain.playlists.PlaylistRepository
import com.akalugin.playlistmaker.domain.playlists.models.Playlist
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class PlaylistRepositoryImpl(
    private val appDatabase: AppDatabase,
    private val fileRepository: FileRepository,
) : PlaylistRepository {
    private val playlistsDao
        get() = appDatabase.playlistsDao()

    override suspend fun add(playlist: Playlist, imageUri: Uri?) {
        val playlistForSaving = if (imageUri == null)
            playlist
        else
            playlist.copy(imagePath = fileRepository.saveImageToPrivateStorage(imageUri))

        playlistsDao.addPlaylist(PlaylistMapper.map(playlistForSaving))
    }

    override fun getPlaylistsWithTrackCount(): Flow<List<Playlist>> =
        playlistsDao.getPlaylistsWithTrackCount().map { it.map(PlaylistMapper::map) }
}