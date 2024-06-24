package com.akalugin.playlistmaker.domain.favorites

import com.akalugin.playlistmaker.domain.track.models.Track
import kotlinx.coroutines.flow.Flow

interface FavoriteTracksInteractor {
    val favorites: Flow<List<Track>>

    suspend fun addToFavorites(track: Track)
    suspend fun removeFromFavorites(track: Track)
}