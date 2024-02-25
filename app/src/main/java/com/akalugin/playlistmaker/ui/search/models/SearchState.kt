package com.akalugin.playlistmaker.ui.search.models

import com.akalugin.playlistmaker.domain.search.models.Track

sealed interface SearchState {
    data object Loading : SearchState

    data class SearchResult(
        val tracks: List<Track>,
    ) : SearchState

    data object EmptyResult : SearchState

    data class History(
        val tracks: List<Track>,
    ) : SearchState

    data object Error : SearchState

    data object Empty : SearchState
}