package com.akalugin.playlistmaker.ui.player.models

data class AudioPlayerScreenState(
    val playerState: PlayerState,
    val isPlaying: Boolean,
    val isFavorite: Boolean,
    val currentPosition: String,
    val isBottomSheetVisible: Boolean,
) {
    enum class PlayerState {
        NO_PREVIEW_AVAILABLE,
        LOADING,
        READY,
    }
}
