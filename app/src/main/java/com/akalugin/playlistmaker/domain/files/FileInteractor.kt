package com.akalugin.playlistmaker.domain.files

import android.net.Uri

interface FileInteractor {
    suspend fun saveImageToPrivateStorage(uri: Uri): String
}