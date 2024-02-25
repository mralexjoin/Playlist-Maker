package com.akalugin.playlistmaker.domain.search.tracks

import com.akalugin.playlistmaker.domain.search.consumer.ConsumerData
import com.akalugin.playlistmaker.domain.search.models.Track

interface TracksRepository {
    fun searchTracks(expression: String): ConsumerData<List<Track>>
}
