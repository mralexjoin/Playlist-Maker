package com.akalugin.playlistmaker.ui.library.playlists.creation.view_model

import android.net.Uri
import androidx.annotation.StringRes
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.akalugin.playlistmaker.R
import com.akalugin.playlistmaker.domain.playlists.PlaylistInteractor
import com.akalugin.playlistmaker.domain.playlists.models.Playlist
import com.akalugin.playlistmaker.ui.library.playlists.creation.models.NewPlaylistScreenState
import com.akalugin.playlistmaker.ui.utils.ClickDebounce
import com.akalugin.playlistmaker.ui.utils.SingleLiveEvent
import kotlinx.coroutines.launch

class NewPlaylistViewModel(
    private val playlistInteractor: PlaylistInteractor,
) : ViewModel() {

    private var state = NewPlaylistScreenState()
        set(value) {
            field = value
            _stateLiveData.postValue(state)
        }

    private val _stateLiveData = MutableLiveData(state)
    val stateLiveData: LiveData<NewPlaylistScreenState>
        get() = _stateLiveData

    private val _showToast = SingleLiveEvent<Pair<Int, Array<Any>>>()
    val showToast: LiveData<Pair<Int, Array<Any>>>
        get() = _showToast


    private val clickDebounce = ClickDebounce(viewModelScope)
    fun clickDebounce(function: () -> Unit) = clickDebounce.debounce(function)

    fun saveImageToPrivateStorage(uri: Uri?) {
        if (uri == null) {
            return
        }

        state = state.copy(imageUri = uri)
    }

    fun onPlaylistNameChanged(text: CharSequence?) {
        state = state.copy(playlistName = text.toString())
    }

    fun onPlaylistDescriptionChanged(text: CharSequence?) {
        state = state.copy(description = text.toString())
    }

    fun createPlaylist() {
        viewModelScope.launch {
            val playlist = Playlist(
                name = state.playlistName,
                description = state.description?.ifEmpty { null },
            )
            playlistInteractor.createPlaylist(playlist, state.imageUri)
            showToast(R.string.playlist_created_message, playlist.name)
            state = state.copy(close = true)
        }
    }

    private fun showToast(@StringRes stringResId: Int, vararg params: Any) {
        _showToast.postValue(stringResId to arrayOf(*params))
    }
}