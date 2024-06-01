package com.akalugin.playlistmaker.data.files.mapper

import com.akalugin.playlistmaker.domain.files.models.PermissionResponse
import com.markodevcic.peko.PermissionResult

object PermissionResponseMapper {
    fun map(permissionResult: PermissionResult) = when (permissionResult) {
        is PermissionResult.Granted -> PermissionResponse.Granted(
            permissionResult.permission
        )

        is PermissionResult.Denied.NeedsRationale -> PermissionResponse.NeedsRationale(
            permissionResult.permission
        )

        is PermissionResult.Denied.DeniedPermanently -> PermissionResponse.DeniedPermanently(
            permissionResult.permission
        )

        is PermissionResult.Denied -> PermissionResponse.Denied(
            permissionResult.permission
        )

        is PermissionResult.Cancelled -> PermissionResponse.Cancelled
    }
}