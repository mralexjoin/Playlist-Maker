package com.akalugin.playlistmaker.ui.library.favorites.models

import com.akalugin.playlistmaker.domain.search.models.Track

interface FavoritesState {
    data object Loading : FavoritesState

    data class Content(
        val tracks: List<Track>,
    ) : FavoritesState

    data object Empty : FavoritesState
}