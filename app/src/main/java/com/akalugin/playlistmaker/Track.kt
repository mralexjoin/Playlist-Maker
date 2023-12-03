package com.akalugin.playlistmaker

import java.text.SimpleDateFormat
import java.util.Locale

data class Track(
    val trackId: Int,
    val trackName: String,
    val artistName: String,
    val trackTimeMillis: Int,
    val artworkUrl100: String,
) {
    val trackTime: String
        get() = SimpleDateFormat("mm:ss", Locale.getDefault()).format(trackTimeMillis)
}
