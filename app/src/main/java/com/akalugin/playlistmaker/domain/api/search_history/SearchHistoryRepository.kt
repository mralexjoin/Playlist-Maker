package com.akalugin.playlistmaker.domain.api.search_history

import com.akalugin.playlistmaker.domain.models.Track

interface SearchHistoryRepository {
    var onItemsChangedListener: OnItemsChangedListener?
    fun add(track: Track)
    fun clear()

    interface OnItemsChangedListener {
        fun onItemsChanged(tracks: List<Track>)
    }
}