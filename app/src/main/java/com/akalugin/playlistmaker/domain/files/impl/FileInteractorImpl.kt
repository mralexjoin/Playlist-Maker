package com.akalugin.playlistmaker.domain.files.impl

import android.net.Uri
import com.akalugin.playlistmaker.domain.files.FileInteractor
import com.akalugin.playlistmaker.domain.files.FileRepository
import kotlinx.coroutines.flow.Flow

class FileInteractorImpl(
    private val fileRepository: FileRepository,
) : FileInteractor {
    override suspend fun saveImageToPrivateStorage(uri: Uri): String =
        fileRepository.saveImageToPrivateStorage(uri)
}