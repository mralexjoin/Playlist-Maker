package com.akalugin.playlistmaker.domain.search.impl

import com.akalugin.playlistmaker.domain.search.history.SearchHistoryInteractor
import com.akalugin.playlistmaker.domain.search.history.SearchHistoryRepository
import com.akalugin.playlistmaker.domain.track.models.Track
import kotlinx.coroutines.flow.Flow

class SearchHistoryInteractorImpl(
    private val searchHistoryRepository: SearchHistoryRepository,
) : SearchHistoryInteractor {

    override suspend fun getTracks(): Flow<List<Track>> = searchHistoryRepository.getHistory()

    override suspend fun addTrack(track: Track) {
        searchHistoryRepository.add(track)
    }

    override fun clearTracks() {
        searchHistoryRepository.clear()
    }
}