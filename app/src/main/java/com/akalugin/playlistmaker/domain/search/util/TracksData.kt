package com.akalugin.playlistmaker.domain.search.util

sealed interface TracksData<T> {
    data class Data<T>(val value: T) : TracksData<T>
    data class Error<T>(val error: TracksSearchError) : TracksData<T>
}