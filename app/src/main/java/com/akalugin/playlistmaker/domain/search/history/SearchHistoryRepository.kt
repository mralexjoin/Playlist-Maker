package com.akalugin.playlistmaker.domain.search.history

import com.akalugin.playlistmaker.domain.track.models.Track
import kotlinx.coroutines.flow.Flow

interface SearchHistoryRepository {
    suspend fun getHistory(): Flow<List<Track>>
    suspend fun add(track: Track)
    fun clear()
}