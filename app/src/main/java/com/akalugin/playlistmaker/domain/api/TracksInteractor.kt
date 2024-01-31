package com.akalugin.playlistmaker.domain.api

import com.akalugin.playlistmaker.domain.models.Track
import com.akalugin.playlistmaker.domain.consumer.Consumer

interface TracksInteractor {
    fun searchTracks(expression: String, consumer: Consumer<List<Track>>)
}