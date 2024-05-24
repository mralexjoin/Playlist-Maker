package com.akalugin.playlistmaker.domain.search.history

import com.akalugin.playlistmaker.domain.search.models.Track

interface SearchHistoryInteractor {
    val searchHistoryRepository: SearchHistoryRepository

    var onItemsChangedListener: OnItemsChangedListener?
    fun addTrack(track: Track)
    fun clearTracks()

    fun interface OnItemsChangedListener {
        fun onItemsChanged(tracks: List<Track>)
    }
}