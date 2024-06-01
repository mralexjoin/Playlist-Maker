package com.akalugin.playlistmaker.data.playlists.impl

import com.akalugin.playlistmaker.data.db.AppDatabase
import com.akalugin.playlistmaker.data.db.playlists.mapper.PlaylistMapper
import com.akalugin.playlistmaker.domain.playlists.PlaylistRepository
import com.akalugin.playlistmaker.domain.playlists.models.Playlist

class PlaylistRepositoryImpl(
    private val appDatabase: AppDatabase,
) : PlaylistRepository {
    private val playlistsDao
        get() = appDatabase.playlistsDao()

    override suspend fun add(playlist: Playlist) {
        playlistsDao.addPlaylist(PlaylistMapper.map(playlist))
    }
}