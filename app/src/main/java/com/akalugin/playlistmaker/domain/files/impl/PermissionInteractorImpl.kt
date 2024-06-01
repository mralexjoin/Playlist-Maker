package com.akalugin.playlistmaker.domain.files.impl

import com.akalugin.playlistmaker.domain.files.PermissionInteractor
import com.akalugin.playlistmaker.domain.files.PermissionRepository

class PermissionInteractorImpl(
    private val permissionRepository: PermissionRepository,
) : PermissionInteractor {
    override suspend fun requestPermissions(permissions: Array<String>) =
        permissionRepository.requestPermissions(permissions)
}