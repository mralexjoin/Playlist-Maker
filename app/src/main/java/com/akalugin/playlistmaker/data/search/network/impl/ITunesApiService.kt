package com.akalugin.playlistmaker.data.search.network.impl

import com.akalugin.playlistmaker.data.search.dto.TrackSearchResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface ITunesApiService {
    @GET("/search?entity=song")
    suspend fun searchTrack(@Query("term") text: String): TrackSearchResponse
}