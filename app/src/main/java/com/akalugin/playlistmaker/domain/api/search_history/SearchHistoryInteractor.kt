package com.akalugin.playlistmaker.domain.api.search_history

import com.akalugin.playlistmaker.domain.models.Track

interface SearchHistoryInteractor {
    val searchHistoryRepository: SearchHistoryRepository

    var onItemsChangedListener: OnItemsChangedListener?
    fun addTrack(track: Track)
    fun clearTracks()

    interface OnItemsChangedListener {
        fun onItemsChanged(tracks: List<Track>)
    }
}