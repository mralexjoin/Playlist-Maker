package com.akalugin.playlistmaker.ui.library.playlists.creation.models

import android.net.Uri

data class EditPlaylistScreenState(
    val imageUri: Uri? = null,
    val close: Boolean = false,
    val needsExitConfirmation: Boolean = false,
    val operation: Operation = Operation.CREATE,
    val createPlaylistButtonEnabled: Boolean = false,
) {
    enum class Operation {
        CREATE,
        EDIT
    }
}