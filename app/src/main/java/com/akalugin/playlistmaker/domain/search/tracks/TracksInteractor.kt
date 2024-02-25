package com.akalugin.playlistmaker.domain.search.tracks

import com.akalugin.playlistmaker.domain.search.consumer.Consumer
import com.akalugin.playlistmaker.domain.search.models.Track

interface TracksInteractor {
    fun searchTracks(expression: String, consumer: Consumer<List<Track>>)
}