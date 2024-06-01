package com.akalugin.playlistmaker.ui.library.favorites.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.akalugin.playlistmaker.domain.favorites.FavoriteTracksInteractor
import com.akalugin.playlistmaker.domain.search.models.Track
import com.akalugin.playlistmaker.ui.library.favorites.models.FavoritesState
import kotlinx.coroutines.launch

class FavoritesViewModel(
    private val favoriteTracksInteractor: FavoriteTracksInteractor,
) : ViewModel() {

    private val _stateLiveData = MutableLiveData<FavoritesState>()
    val stateLiveData: LiveData<FavoritesState>
        get() = _stateLiveData

    fun getFavorites() {
        _stateLiveData.postValue(FavoritesState.Loading)
        viewModelScope.launch {
            favoriteTracksInteractor.favorites.collect(::processTracks)
        }
    }

    private fun processTracks(tracks: List<Track>) {
        _stateLiveData.postValue(
            if (tracks.isEmpty()) FavoritesState.Empty else FavoritesState.Content(tracks)
        )
    }
}