package com.akalugin.playlistmaker.data.network

import com.akalugin.playlistmaker.data.NetworkClient
import com.akalugin.playlistmaker.data.dto.NetworkRequest
import com.akalugin.playlistmaker.data.dto.NetworkResponse
import com.akalugin.playlistmaker.data.dto.TrackSearchRequest
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitNetworkClient : NetworkClient {
    private const val I_TUNES_BASE_URL = "https://itunes.apple.com"

    private val client: Retrofit by lazy {
        Retrofit.Builder().baseUrl(I_TUNES_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    private val api: ITunesRetrofitApiService by lazy {
        client.create(ITunesRetrofitApiService::class.java)
    }

    override fun doRequest(request: NetworkRequest): NetworkResponse =
        when (request) {
            is TrackSearchRequest -> {
                val response = api.searchTrack(request.expression).execute()
                val body = response.body() ?: NetworkResponse()
                body.apply { resultCode = response.code() }
            }
        }
}