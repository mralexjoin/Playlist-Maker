package com.akalugin.playlistmaker.domain.files

import android.net.Uri
import kotlinx.coroutines.flow.Flow

interface FileRepository {
    suspend fun saveImageToPrivateStorage(uri: Uri): Flow<String>
}