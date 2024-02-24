package com.akalugin.playlistmaker.data.network

import com.akalugin.playlistmaker.data.dto.TrackSearchResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ITunesRetrofitApiService {
    @GET("/search?entity=song")
    fun searchTrack(@Query("term") text: String): Call<TrackSearchResponse>
}