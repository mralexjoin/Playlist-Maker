package com.akalugin.playlistmaker.domain.impl

import com.akalugin.playlistmaker.domain.api.TracksInteractor
import com.akalugin.playlistmaker.domain.api.TracksRepository
import com.akalugin.playlistmaker.domain.consumer.Consumer
import com.akalugin.playlistmaker.domain.consumer.ConsumerData
import com.akalugin.playlistmaker.domain.models.Resource
import com.akalugin.playlistmaker.domain.models.Track
import java.util.concurrent.Executors

class TracksInteractorImpl(private val tracksRepository: TracksRepository) : TracksInteractor {
    private val executor = Executors.newCachedThreadPool()

    override fun searchTracks(expression: String, consumer: Consumer<List<Track>>) =
        executor.execute {
            val data = when (val tracksResponse = tracksRepository.searchTracks(expression)) {
                is Resource.Success -> {
                    ConsumerData.Data(tracksResponse.data)
                }

                is Resource.Error -> {
                    ConsumerData.Error(tracksResponse.message)
                }
            }
            consumer.consume(data)
        }
}