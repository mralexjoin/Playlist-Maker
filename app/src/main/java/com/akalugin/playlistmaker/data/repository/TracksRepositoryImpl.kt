package com.akalugin.playlistmaker.data.repository

import com.akalugin.playlistmaker.data.NetworkClient
import com.akalugin.playlistmaker.data.dto.TrackSearchRequest
import com.akalugin.playlistmaker.data.dto.TrackSearchResponse
import com.akalugin.playlistmaker.data.mapper.TracksMapper
import com.akalugin.playlistmaker.domain.api.tracks.TracksRepository
import com.akalugin.playlistmaker.domain.consumer.ConsumerData
import com.akalugin.playlistmaker.domain.models.Track

class TracksRepositoryImpl(private val networkClient: NetworkClient) : TracksRepository {
    override fun searchTracks(expression: String): ConsumerData<List<Track>> =
        try {
            val response = networkClient.doRequest(TrackSearchRequest(expression))
            if (response is TrackSearchResponse) {
                val tracks = response.results.map(TracksMapper::map)
                ConsumerData.Data(tracks)
            } else {
                ConsumerData.Error(response.resultCode.toString())
            }
        } catch (ex: Exception) {
            ConsumerData.Error(ex.message ?: "")
        }
}