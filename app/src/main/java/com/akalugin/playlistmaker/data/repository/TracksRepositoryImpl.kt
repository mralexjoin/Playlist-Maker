package com.akalugin.playlistmaker.data.repository

import com.akalugin.playlistmaker.data.NetworkClient
import com.akalugin.playlistmaker.data.dto.TrackSearchRequest
import com.akalugin.playlistmaker.data.dto.TrackSearchResponse
import com.akalugin.playlistmaker.data.mapper.TracksMapper
import com.akalugin.playlistmaker.domain.api.TracksRepository
import com.akalugin.playlistmaker.domain.models.Resource
import com.akalugin.playlistmaker.domain.models.Track

class TracksRepositoryImpl(private val networkClient: NetworkClient) : TracksRepository {
    override fun searchTracks(expression: String): Resource<List<Track>> =
        try {
            val response = networkClient.doRequest(TrackSearchRequest(expression))
            if (response is TrackSearchResponse) {
                val tracks = response.results.map(TracksMapper::map)
                Resource.Success(tracks)
            } else {
                Resource.Error(response.resultCode.toString())
            }
        } catch (ex: Exception) {
            Resource.Error(ex.message ?: "")
        }
}