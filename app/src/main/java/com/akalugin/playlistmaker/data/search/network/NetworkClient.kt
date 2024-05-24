package com.akalugin.playlistmaker.data.search.network

import com.akalugin.playlistmaker.data.search.dto.NetworkResponse

interface NetworkClient {
    suspend fun doRequest(request: Any): NetworkResponse
}