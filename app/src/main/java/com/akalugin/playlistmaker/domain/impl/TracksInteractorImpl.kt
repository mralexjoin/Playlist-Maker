package com.akalugin.playlistmaker.domain.impl

import com.akalugin.playlistmaker.domain.api.tracks.TracksInteractor
import com.akalugin.playlistmaker.domain.api.tracks.TracksRepository
import com.akalugin.playlistmaker.domain.consumer.Consumer
import com.akalugin.playlistmaker.domain.models.Track
import java.util.concurrent.Executors

class TracksInteractorImpl(private val tracksRepository: TracksRepository) : TracksInteractor {
    private val executor = Executors.newCachedThreadPool()

    override fun searchTracks(expression: String, consumer: Consumer<List<Track>>) =
        executor.execute {
            consumer.consume(tracksRepository.searchTracks(expression))
        }
}