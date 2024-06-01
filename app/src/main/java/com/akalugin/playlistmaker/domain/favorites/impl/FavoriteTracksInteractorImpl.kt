package com.akalugin.playlistmaker.domain.favorites.impl

import com.akalugin.playlistmaker.domain.favorites.FavoriteTracksInteractor
import com.akalugin.playlistmaker.domain.favorites.FavoriteTracksRepository
import com.akalugin.playlistmaker.domain.track.models.Track
import kotlinx.coroutines.flow.Flow

class FavoriteTracksInteractorImpl(
    private val favoriteTracksRepository: FavoriteTracksRepository,
) : FavoriteTracksInteractor {
    override val favorites: Flow<List<Track>>
        get() = favoriteTracksRepository.favorites

    override suspend fun addToFavorites(track: Track) =
        favoriteTracksRepository.addToFavorites(track)

    override suspend fun removeFromFavorites(track: Track) =
        favoriteTracksRepository.removeFromFavorites(track)
}