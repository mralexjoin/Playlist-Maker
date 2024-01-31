package com.akalugin.playlistmaker.domain.api

import com.akalugin.playlistmaker.domain.models.Resource
import com.akalugin.playlistmaker.domain.models.Track

interface TracksRepository {
    fun searchTracks(expression: String): Resource<List<Track>>
}
