package com.akalugin.playlistmaker.data.favorites.impl

import com.akalugin.playlistmaker.data.db.AppDatabase
import com.akalugin.playlistmaker.data.db.mapper.FavoriteTrackMapper
import com.akalugin.playlistmaker.domain.favorites.FavoriteTracksRepository
import com.akalugin.playlistmaker.domain.search.models.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FavoriteTracksRepositoryImpl(
    private val appDatabase: AppDatabase,
) : FavoriteTracksRepository {
    private val trackDao
        get() = appDatabase.trackDao()
    override val favorites: Flow<List<Track>>
        get() = flow {
            emit(trackDao.get().map(FavoriteTrackMapper::map))
        }

    override suspend fun addToFavorites(track: Track) =
        trackDao.add(FavoriteTrackMapper.map(track))

    override suspend fun removeFromFavorites(track: Track) =
        trackDao.remove(FavoriteTrackMapper.map(track))

    override suspend fun setIsFavorite(tracks: List<Track>) =
        filterFavoriteIds(
            tracks.map { it.trackId },
        ).let { favoriteTrackIds ->
            tracks.forEach { track -> track.isFavorite = favoriteTrackIds.contains(track.trackId) }
        }

    override suspend fun filterFavoriteIds(trackIds: List<Int>): Set<Int> =
        trackDao.filterIds(trackIds).toSet()
}