package com.akalugin.playlistmaker.ui.library.playlists.creation.view_model

import android.net.Uri
import androidx.annotation.StringRes
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.akalugin.playlistmaker.R
import com.akalugin.playlistmaker.domain.files.FileInteractor
import com.akalugin.playlistmaker.domain.playlists.PlaylistInteractor
import com.akalugin.playlistmaker.domain.playlists.models.Playlist
import com.akalugin.playlistmaker.ui.library.playlists.creation.models.EditPlaylistScreenState
import com.akalugin.playlistmaker.ui.utils.ClickDebounce
import com.akalugin.playlistmaker.ui.utils.SingleLiveEvent
import kotlinx.coroutines.launch
import java.io.File

class EditPlaylistViewModel(
    private val playlist: Playlist?,
    private val playlistInteractor: PlaylistInteractor,
    private val fileInteractor: FileInteractor,
) : ViewModel() {

    private var state = EditPlaylistScreenState()
        set(value) {
            field = value
            _stateLiveData.postValue(state)
        }

    private val _stateLiveData = MutableLiveData(state)
    val stateLiveData: LiveData<EditPlaylistScreenState>
        get() = _stateLiveData

    private val _showToast = SingleLiveEvent<Pair<Int, Array<Any>>>()
    val showToast: LiveData<Pair<Int, Array<Any>>>
        get() = _showToast

    init {
        playlist?.let { playlist ->
            with(playlist) {
                state = state.copy(
                    imageUri = imagePath?.let { Uri.fromFile(File(it)) },
                    needsExitConfirmation = false,
                    operation = EditPlaylistScreenState.Operation.EDIT,
                    createPlaylistButtonEnabled = true,
                )
            }
        }
    }

    private val clickDebounce = ClickDebounce(viewModelScope)
    fun clickDebounce(function: () -> Unit) = clickDebounce.debounce(function)

    private var playlistName: String = ""
    private var playlistDescription: String? = null

    fun saveImageToPrivateStorage(uri: Uri?) {
        if (uri == null) {
            return
        }

        state = state.copy(imageUri = uri)
        updateNeedsConfirmation()
    }

    fun onPlaylistNameChanged(text: CharSequence?) {
        playlistName = text.toString()
        updateCreateButtonEnabled()
        updateNeedsConfirmation()
    }


    fun onPlaylistDescriptionChanged(text: CharSequence?) {
        playlistDescription = text.toString()
        updateNeedsConfirmation()
    }

    fun savePlaylist() {
        viewModelScope.launch {
            with(state) {
                val imagePath = imageUri?.let { fileInteractor.saveImageToPrivateStorage(it) }
                val description = playlistDescription?.ifEmpty { null }

                playlist?.let {
                    playlistInteractor.updatePlaylist(
                        it.copy(
                            imagePath = imagePath,
                            name = playlistName,
                            description = description,
                        )
                    )
                    showToast(R.string.playlist_updated_message, playlistName)
                } ?: run {
                    playlistInteractor.createPlaylist(
                        Playlist(
                            imagePath = imagePath,
                            name = playlistName,
                            description = description,
                        )
                    )
                    showToast(R.string.playlist_created_message, playlistName)
                }

                state = copy(
                    close = true,
                )
            }
        }
    }

    private fun showToast(@StringRes stringResId: Int, vararg params: Any) {
        _showToast.postValue(stringResId to arrayOf(*params))
    }

    private fun updateNeedsConfirmation() {
        with(state) {
            state = copy(
                needsExitConfirmation = playlist == null &&
                        (imageUri != null
                                || playlistName.isNotEmpty()
                                || !playlistDescription.isNullOrEmpty()),
            )
        }
    }

    private fun updateCreateButtonEnabled() {
        state = state.copy(
            createPlaylistButtonEnabled = playlistName.isNotEmpty()
        )
    }
}