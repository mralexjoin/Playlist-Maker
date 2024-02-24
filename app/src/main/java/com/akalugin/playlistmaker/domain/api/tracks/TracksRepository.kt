package com.akalugin.playlistmaker.domain.api.tracks

import com.akalugin.playlistmaker.domain.consumer.ConsumerData
import com.akalugin.playlistmaker.domain.models.Track

interface TracksRepository {
    fun searchTracks(expression: String): ConsumerData<List<Track>>
}
