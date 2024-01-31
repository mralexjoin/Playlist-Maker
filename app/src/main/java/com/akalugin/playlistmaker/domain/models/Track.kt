package com.akalugin.playlistmaker.domain.models

import java.io.Serializable

data class Track(
    val trackId: Int,
    val trackName: String,
    val artistName: String,
    val trackTime: String,
    val artworkUrl100: String,
    val collectionName: String?,
    val releaseYear: String,
    val primaryGenreName: String,
    val country: String,
    val previewUrl: String,
) : Serializable
