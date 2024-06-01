package com.akalugin.playlistmaker.domain.favorites

import com.akalugin.playlistmaker.domain.track.models.Track
import kotlinx.coroutines.flow.Flow

interface FavoriteTracksRepository {
    val favorites: Flow<List<Track>>

    suspend fun setIsFavorite(tracks: List<Track>)
    suspend fun filterFavoriteIds(trackIds: List<Int>): Set<Int>

    suspend fun addToFavorites(track: Track)
    suspend fun removeFromFavorites(track: Track)
}