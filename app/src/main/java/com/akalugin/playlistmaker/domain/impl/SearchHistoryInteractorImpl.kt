package com.akalugin.playlistmaker.domain.impl

import com.akalugin.playlistmaker.domain.api.search_history.SearchHistoryInteractor
import com.akalugin.playlistmaker.domain.api.search_history.SearchHistoryRepository
import com.akalugin.playlistmaker.domain.models.Track

class SearchHistoryInteractorImpl(override val searchHistoryRepository: SearchHistoryRepository) :
    SearchHistoryInteractor {

    override var onItemsChangedListener: SearchHistoryInteractor.OnItemsChangedListener? = null
        set(value) {
            field = value
            searchHistoryRepository.onItemsChangedListener =
                object : SearchHistoryRepository.OnItemsChangedListener {
                    override fun onItemsChanged(tracks: List<Track>) {
                        onItemsChangedListener?.onItemsChanged(tracks)
                    }
                }
        }

    override fun addTrack(track: Track) {
        searchHistoryRepository.add(track)
    }

    override fun clearTracks() {
        searchHistoryRepository.clear()
    }
}