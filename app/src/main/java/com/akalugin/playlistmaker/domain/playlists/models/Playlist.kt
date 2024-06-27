package com.akalugin.playlistmaker.domain.playlists.models

import com.akalugin.playlistmaker.domain.track.models.Track
import java.io.Serializable

data class Playlist(
    val playlistId: Int = 0,
    val name: String,
    val imagePath: String? = null,
    val description: String? = null,
    val tracks: List<Track> = emptyList(),
    val trackCount: Int = tracks.size,
) : Serializable
