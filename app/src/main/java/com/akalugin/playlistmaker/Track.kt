package com.akalugin.playlistmaker

import java.io.Serializable
import java.text.SimpleDateFormat
import java.util.Locale

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
) : Serializable {
    val trackTime: String
        get() = SimpleDateFormat("mm:ss", Locale.getDefault()).format(trackTimeMillis)

    val releaseYear: String
        get() = releaseDate.substring(0, YEAR_LENGTH)

    private companion object {
        const val YEAR_LENGTH = 4
    }
}
