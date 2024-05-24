package com.akalugin.playlistmaker.domain.search.tracks

import com.akalugin.playlistmaker.domain.search.models.Track
import com.akalugin.playlistmaker.domain.search.util.TracksData
import kotlinx.coroutines.flow.Flow

interface TracksRepository {
    fun searchTracks(expression: String): Flow<TracksData<List<Track>>>
}
