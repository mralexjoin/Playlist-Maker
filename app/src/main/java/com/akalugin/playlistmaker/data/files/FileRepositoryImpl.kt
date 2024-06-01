package com.akalugin.playlistmaker.data.files

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Environment
import com.akalugin.playlistmaker.BuildConfig
import com.akalugin.playlistmaker.domain.files.FileRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import java.io.File
import java.io.FileOutputStream

class FileRepositoryImpl(applicationContext: Context) : FileRepository {
    private val filePath = File(
        applicationContext.getExternalFilesDir(Environment.DIRECTORY_PICTURES),
        BuildConfig.IMAGE_DIRECTORY_NAME
    )

    private val contentResolver = applicationContext.contentResolver

    init {
        if (!filePath.exists()) {
            filePath.mkdirs()
        }
    }

    override suspend fun saveImageToPrivateStorage(uri: Uri) = flow {
        val file = File.createTempFile(IMAGE_FILE_PREFIX, IMAGE_FILE_SUFFIX, filePath)
        val inputStream = contentResolver.openInputStream(uri)
        val outputStream = FileOutputStream(file)
        BitmapFactory
            .decodeStream(inputStream)
            ?.compress(Bitmap.CompressFormat.JPEG, 30, outputStream)

        inputStream?.close()
        outputStream.close()

        emit(file.path)
    }
        .flowOn(Dispatchers.IO)

    private companion object {
        const val IMAGE_FILE_PREFIX = "image_"
        const val IMAGE_FILE_SUFFIX = ".jpg"
    }
}