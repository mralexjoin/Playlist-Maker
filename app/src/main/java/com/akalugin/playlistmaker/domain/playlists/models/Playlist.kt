package com.akalugin.playlistmaker.domain.playlists.models

import com.akalugin.playlistmaker.domain.track.models.Track

data class Playlist(
    val playlistId: Int,
    val name: String,
    val imagePath: String?,
    val description: String?,
    val tracks: List<Track>,
)
