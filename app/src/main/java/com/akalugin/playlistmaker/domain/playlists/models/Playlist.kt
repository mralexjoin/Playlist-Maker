package com.akalugin.playlistmaker.domain.playlists.models

data class Playlist(
    val playlistId: Int = 0,
    val name: String,
    val imagePath: String? = null,
    val description: String? = null,
    val trackCount: Int = 0,
)
