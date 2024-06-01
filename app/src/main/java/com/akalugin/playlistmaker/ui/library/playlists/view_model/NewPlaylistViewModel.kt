package com.akalugin.playlistmaker.ui.library.playlists.view_model

import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.Manifest.permission.READ_MEDIA_IMAGES
import android.Manifest.permission.READ_MEDIA_VISUAL_USER_SELECTED
import android.net.Uri
import android.os.Build
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.akalugin.playlistmaker.domain.files.FileInteractor
import com.akalugin.playlistmaker.domain.files.PermissionInteractor
import com.akalugin.playlistmaker.domain.playlists.PlaylistInteractor
import kotlinx.coroutines.launch

class NewPlaylistViewModel(
    private val playlistInteractor: PlaylistInteractor,
    private val fileInteractor: FileInteractor,
    private val permissionInteractor: PermissionInteractor,
) : ViewModel() {

    fun saveImageToPrivateStorage(uri: Uri?) {
        if (uri == null) {
            return
        }

        viewModelScope.launch {
            fileInteractor.saveImageToPrivateStorage(uri).collect {
            }
        }
    }

    fun requestPermissionsAndRun(function: () -> Unit) = function()

//        viewModelScope.launch {
//        val permissionsNotGranted = FILE_PERMISSIONS.toMutableSet()
//        permissionInteractor.requestPermissions(FILE_PERMISSIONS).collect { permissionResponse ->
//            when (permissionResponse) {
//                is PermissionResponse.Granted -> {
//                    permissionsNotGranted.remove(permissionResponse.permission)
//                    Log.d(
//                        "PERMISSION",
//                        "GRANTED ${permissionResponse.permission}"
//                    )
//                }
//
//                is PermissionResponse.Denied -> {
//                    Log.d(
//                        "PERMISSION",
//                        "DENIED ${permissionResponse.permission}"
//                    )
//                }
//
//                is PermissionResponse.DeniedPermanently -> {
//                    Log.d(
//                        "PERMISSION",
//                        "DENIED PERMANENTLY ${permissionResponse.permission}"
//                    )
//                }
//
//                is PermissionResponse.NeedsRationale -> {
//                    Log.d(
//                        "PERMISSION",
//                        "NEEDS RATIONALE ${permissionResponse.permission}"
//                    )
//                }
//
//                is PermissionResponse.Cancelled -> {
//                    Log.d(
//                        "PERMISSION",
//                        "CANCELED"
//                    )
//                }
//            }
//        }
//        if (permissionsNotGranted.isEmpty()) {
//            function()
//        }
//    }

    companion object {
        private val FILE_PERMISSIONS =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
                arrayOf(READ_MEDIA_IMAGES, READ_MEDIA_VISUAL_USER_SELECTED)
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                arrayOf(READ_MEDIA_IMAGES)
            } else {
                arrayOf(READ_EXTERNAL_STORAGE)
            }
    }
}