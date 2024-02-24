package com.akalugin.playlistmaker.data.search.network

import com.akalugin.playlistmaker.data.search.dto.NetworkResponse

interface NetworkClient {
    fun doRequest(request: Any): NetworkResponse
}