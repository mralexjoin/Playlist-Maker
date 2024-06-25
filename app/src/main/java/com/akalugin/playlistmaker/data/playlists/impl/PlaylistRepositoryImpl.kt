package com.akalugin.playlistmaker.data.playlists.impl

import com.akalugin.playlistmaker.data.db.AppDatabase
import com.akalugin.playlistmaker.data.db.playlists.entity.PlaylistTrackCrossRef
import com.akalugin.playlistmaker.data.db.playlists.mapper.PlaylistMapper
import com.akalugin.playlistmaker.domain.playlists.PlaylistRepository
import com.akalugin.playlistmaker.domain.playlists.models.Playlist
import com.akalugin.playlistmaker.domain.track.models.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class PlaylistRepositoryImpl(
    private val appDatabase: AppDatabase,
) : PlaylistRepository {
    private val playlistsDao
        get() = appDatabase.playlistsDao()

    override suspend fun add(playlist: Playlist) {
        playlistsDao.insertPlaylist(PlaylistMapper.mapToPlaylistEntity(playlist))
    }

    override suspend fun update(playlist: Playlist) {
        playlistsDao.updatePlaylist(PlaylistMapper.mapToPlaylistEntity(playlist))
    }

    override fun getPlaylistsWithTrackCount(): Flow<List<Playlist>> =
        playlistsDao.getPlaylistsWithTrackCount().map { it.map(PlaylistMapper::mapToPlaylist) }

    override fun getPlaylistsWithTracks(): Flow<List<Playlist>> =
        playlistsDao.getPlaylistsWithTracks().map {
            it.mapNotNull(PlaylistMapper::mapToPlaylist)
        }

    override suspend fun addTrackToPlaylist(track: Track, playlist: Playlist) {
        playlistsDao.addTrackToPlaylist(PlaylistMapper.mapToTrackEntity(track), playlist.playlistId)
    }

    override fun getPlaylistWithTracks(playlistId: Int): Flow<Playlist?> {
        return playlistsDao.getPlaylistWithTracks(playlistId).map(PlaylistMapper::mapToPlaylist)
    }

    override suspend fun deleteTrackFromPlaylist(trackId: Int, playlistId: Int) {
        playlistsDao.deleteTrackFromPlaylist(PlaylistTrackCrossRef(playlistId, trackId))
    }

    override suspend fun deletePlaylist(playlist: Playlist) {
        playlistsDao.deletePlaylistWithOrphanTracks(
            PlaylistMapper.mapToPlaylistWithTracks(playlist)
        )
    }
}