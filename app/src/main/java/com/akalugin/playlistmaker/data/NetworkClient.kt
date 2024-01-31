package com.akalugin.playlistmaker.data

import com.akalugin.playlistmaker.data.dto.NetworkRequest
import com.akalugin.playlistmaker.data.dto.NetworkResponse

interface NetworkClient {
    fun doRequest(request: NetworkRequest): NetworkResponse
}