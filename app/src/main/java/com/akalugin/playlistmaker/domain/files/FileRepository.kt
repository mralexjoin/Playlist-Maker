package com.akalugin.playlistmaker.domain.files

import android.net.Uri

interface FileRepository {
    suspend fun saveImageToPrivateStorage(uri: Uri): String
}