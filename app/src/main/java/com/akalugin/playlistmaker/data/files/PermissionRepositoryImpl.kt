package com.akalugin.playlistmaker.data.files

import com.akalugin.playlistmaker.data.files.mapper.PermissionResponseMapper
import com.akalugin.playlistmaker.domain.files.PermissionRepository
import com.markodevcic.peko.PermissionRequester
import kotlinx.coroutines.flow.map

class PermissionRepositoryImpl(
    private val requester: PermissionRequester,
) : PermissionRepository {
    override suspend fun requestPermissions(permissions: Array<String>) =
        requester.request(*permissions).map(PermissionResponseMapper::map)
}