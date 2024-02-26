package com.akalugin.playlistmaker.data.search.network.impl

import com.akalugin.playlistmaker.data.search.dto.NetworkResponse
import com.akalugin.playlistmaker.data.search.dto.TrackSearchRequest
import com.akalugin.playlistmaker.data.search.network.NetworkClient

class RetrofitNetworkClient(
    private val iTunesApiService: ITunesApiService,
) : NetworkClient {

    override fun doRequest(request: Any): NetworkResponse =
        when (request) {
            is TrackSearchRequest -> {
                val response = iTunesApiService.searchTrack(request.expression).execute()
                val body = response.body() ?: NetworkResponse()
                body.apply { resultCode = response.code() }
            }
            else -> NetworkResponse().apply { resultCode = 400 }
        }
}