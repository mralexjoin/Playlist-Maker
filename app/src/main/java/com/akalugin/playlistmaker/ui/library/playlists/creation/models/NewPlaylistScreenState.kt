package com.akalugin.playlistmaker.ui.library.playlists.creation.models

import android.net.Uri

data class NewPlaylistScreenState(
    val imageUri: Uri? = null,
    val playlistName: String = "",
    val description: String? = null,
    val close: Boolean = false,
)