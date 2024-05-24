package com.akalugin.playlistmaker.data.search.impl

import com.akalugin.playlistmaker.data.search.dto.TrackSearchRequest
import com.akalugin.playlistmaker.data.search.dto.TrackSearchResponse
import com.akalugin.playlistmaker.data.search.filter.TracksFilter
import com.akalugin.playlistmaker.data.search.mapper.TracksMapper
import com.akalugin.playlistmaker.data.search.network.NetworkClient
import com.akalugin.playlistmaker.domain.search.models.Track
import com.akalugin.playlistmaker.domain.search.tracks.TracksRepository
import com.akalugin.playlistmaker.domain.search.util.TracksSearchError
import com.akalugin.playlistmaker.domain.search.util.TracksData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class TracksRepositoryImpl(private val networkClient: NetworkClient) : TracksRepository {
    override fun searchTracks(expression: String): Flow<TracksData<List<Track>>> = flow {
        val response = networkClient.doRequest(TrackSearchRequest(expression))
        emit(
            if (response is TrackSearchResponse) {
                val tracks = response.results.filter(TracksFilter::filter).map(TracksMapper::map)
                TracksData.Data(tracks)
            } else {
                val error = TracksSearchError.entries.firstOrNull { it.code == response.resultCode }
                TracksData.Error(error ?: TracksSearchError.UNKNOWN_ERROR)
            }
        )
    }
}
