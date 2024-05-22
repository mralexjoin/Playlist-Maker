package com.akalugin.playlistmaker.data.search.network.impl

import com.akalugin.playlistmaker.data.search.dto.NetworkResponse
import com.akalugin.playlistmaker.data.search.dto.TrackSearchRequest
import com.akalugin.playlistmaker.data.search.network.NetworkClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class RetrofitNetworkClient(
    private val iTunesApiService: ITunesApiService,
) : NetworkClient {

    override suspend fun doRequest(request: Any): NetworkResponse = withContext(Dispatchers.IO) {
        try {
            when (request) {
                is TrackSearchRequest -> {
                    val response = iTunesApiService.searchTrack(request.expression)
                    response.apply { resultCode = 200 }
                }

                else -> NetworkResponse().apply { resultCode = 400 }
            }
        } catch (e: Throwable) {
            NetworkResponse().apply { resultCode = 500 }
        }
    }
}