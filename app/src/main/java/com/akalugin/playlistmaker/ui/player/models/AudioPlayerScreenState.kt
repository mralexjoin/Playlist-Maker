package com.akalugin.playlistmaker.ui.player.models

sealed interface AudioPlayerScreenState {
    data object NoPreviewAvailable: AudioPlayerScreenState

    data object Loading: AudioPlayerScreenState

    data class Playing(
        val currentPosition: String,
    ) : AudioPlayerScreenState

    data class Paused(
        val currentPosition: String,
    ) : AudioPlayerScreenState
}
