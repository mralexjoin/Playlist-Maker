package com.akalugin.playlistmaker.domain.files

import com.akalugin.playlistmaker.domain.files.models.PermissionResponse
import kotlinx.coroutines.flow.Flow

interface PermissionRepository {
    suspend fun requestPermissions(permissions: Array<String>): Flow<PermissionResponse>
}