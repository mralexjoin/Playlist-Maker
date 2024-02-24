package com.akalugin.playlistmaker.domain.search.impl

import com.akalugin.playlistmaker.domain.search.consumer.Consumer
import com.akalugin.playlistmaker.domain.search.models.Track
import com.akalugin.playlistmaker.domain.search.tracks.TracksInteractor
import com.akalugin.playlistmaker.domain.search.tracks.TracksRepository
import java.util.concurrent.Executors

class TracksInteractorImpl(private val tracksRepository: TracksRepository) : TracksInteractor {
    private val executor = Executors.newCachedThreadPool()

    override fun searchTracks(expression: String, consumer: Consumer<List<Track>>) =
        executor.execute {
            consumer.consume(tracksRepository.searchTracks(expression))
        }
}