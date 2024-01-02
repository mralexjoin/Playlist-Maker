package com.akalugin.playlistmaker

import java.io.Serializable

data class Track(
    val trackId: Int,
    val trackName: String,
    val artistName: String,
    val trackTimeMillis: Int,
    val artworkUrl100: String,
    val collectionName: String?,
    val releaseDate: String,
    val primaryGenreName: String,
    val country: String,
    val previewUrl: String,
) : Serializable {
    val trackTime: String
        get() = formatMilliseconds(trackTimeMillis)

    val releaseYear: String
        get() = releaseDate.substring(0, YEAR_LENGTH)

    private companion object {
        const val YEAR_LENGTH = 4
    }
}
