package com.akalugin.playlistmaker.domain.search.history

import com.akalugin.playlistmaker.domain.search.models.Track

interface SearchHistoryRepository {
    var onItemsChangedListener: OnItemsChangedListener?
    fun add(track: Track)
    fun clear()

    interface OnItemsChangedListener {
        fun onItemsChanged(tracks: List<Track>)
    }
}