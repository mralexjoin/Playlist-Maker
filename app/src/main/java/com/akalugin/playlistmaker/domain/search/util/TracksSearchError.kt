package com.akalugin.playlistmaker.domain.search.util

enum class TracksSearchError(val code: Int) {
    CLIENT_ERROR(400),
    SERVER_ERROR(500),
    UNKNOWN_ERROR(-1)
}