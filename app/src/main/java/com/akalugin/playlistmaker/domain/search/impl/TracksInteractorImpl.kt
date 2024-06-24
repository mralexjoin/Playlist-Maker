package com.akalugin.playlistmaker.domain.search.impl

import com.akalugin.playlistmaker.domain.track.models.Track
import com.akalugin.playlistmaker.domain.search.util.TracksData
import com.akalugin.playlistmaker.domain.search.tracks.TracksInteractor
import com.akalugin.playlistmaker.domain.search.tracks.TracksRepository
import kotlinx.coroutines.flow.Flow

class TracksInteractorImpl(private val tracksRepository: TracksRepository) : TracksInteractor {
    override fun searchTracks(expression: String): Flow<TracksData<List<Track>>> =
        tracksRepository.searchTracks(expression)
}