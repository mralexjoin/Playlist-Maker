package com.akalugin.playlistmaker.domain.search.history

import com.akalugin.playlistmaker.domain.search.models.Track
import kotlinx.coroutines.flow.Flow

interface SearchHistoryInteractor {
    suspend fun getTracks(): Flow<List<Track>>
    suspend fun addTrack(track: Track)
    fun clearTracks()
}