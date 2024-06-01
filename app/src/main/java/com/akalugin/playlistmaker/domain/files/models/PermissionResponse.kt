package com.akalugin.playlistmaker.domain.files.models

sealed interface PermissionResponse {

    data class Granted(
        val permission: String,
    ) : PermissionResponse

    data class NeedsRationale(
        val permission: String,
    ) : PermissionResponse

    data class DeniedPermanently(
        val permission: String,
    ) : PermissionResponse

    data class Denied(
        val permission: String,
    ) : PermissionResponse

    data object Cancelled : PermissionResponse
}