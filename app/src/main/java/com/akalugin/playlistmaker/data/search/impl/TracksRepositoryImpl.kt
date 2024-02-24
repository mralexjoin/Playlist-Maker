package com.akalugin.playlistmaker.data.search.impl

import com.akalugin.playlistmaker.data.search.network.NetworkClient
import com.akalugin.playlistmaker.data.search.dto.TrackSearchRequest
import com.akalugin.playlistmaker.data.search.dto.TrackSearchResponse
import com.akalugin.playlistmaker.data.search.filter.TracksFilter
import com.akalugin.playlistmaker.data.search.mapper.TracksMapper
import com.akalugin.playlistmaker.domain.search.tracks.TracksRepository
import com.akalugin.playlistmaker.domain.search.consumer.ConsumerData
import com.akalugin.playlistmaker.domain.search.models.Track

class TracksRepositoryImpl(private val networkClient: NetworkClient) : TracksRepository {
    override fun searchTracks(expression: String): ConsumerData<List<Track>> =
        try {
            val response = networkClient.doRequest(TrackSearchRequest(expression))
            if (response is TrackSearchResponse) {
                val tracks = response.results.filter(TracksFilter::filter).map(TracksMapper::map)
                ConsumerData.Data(tracks)
            } else {
                ConsumerData.Error(response.resultCode.toString())
            }
        } catch (ex: Exception) {
            ConsumerData.Error(ex.message ?: "")
        }
}