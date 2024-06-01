package com.akalugin.playlistmaker.ui.search.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.akalugin.playlistmaker.domain.search.history.SearchHistoryInteractor
import com.akalugin.playlistmaker.domain.track.models.Track
import com.akalugin.playlistmaker.domain.search.tracks.TracksInteractor
import com.akalugin.playlistmaker.domain.search.util.TracksData
import com.akalugin.playlistmaker.ui.search.models.SearchState
import com.akalugin.playlistmaker.ui.utils.SingleLiveEvent
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SearchViewModel(
    private val tracksInteractor: TracksInteractor,
    private val searchHistoryInteractor: SearchHistoryInteractor,
) : ViewModel() {

    private var latestSearchText: String? = null

    private val _stateLiveData = MutableLiveData<SearchState>()
    val stateLiveData: LiveData<SearchState>
        get() = _stateLiveData

    private val _showToast = SingleLiveEvent<String>()
    val showToast: LiveData<String>
        get() = _showToast

    private var searchInputHasFocus: Boolean? = null

    private val _clearButtonVisibilityLiveData = MutableLiveData(false)
    val clearButtonVisibilityLiveData: LiveData<Boolean>
        get() = _clearButtonVisibilityLiveData

    private var searchJob: Job? = null

    fun onSearchInputChanged(changedText: String) {
        if (latestSearchText == changedText)
            return
        latestSearchText = changedText
        updateClearButtonVisibility(changedText)

        renderHistoryIfVisibleOrRun {
            searchJob?.cancel()
            searchJob = viewModelScope.launch {
                delay(SEARCH_DEBOUNCE_DELAY)
                searchTracks(changedText)
            }
        }
    }

    fun onSearchInputFocusChanged(hasFocus: Boolean) {
        searchInputHasFocus = hasFocus
        renderHistoryIfVisibleOrRun(null)
    }

    private fun updateClearButtonVisibility(changedText: String) {
        _clearButtonVisibilityLiveData.value = changedText.isNotEmpty()
    }

    fun searchTracks(searchText: String) {
        searchJob?.cancel()
        if (searchText.isEmpty()) {
            return
        }
        renderState(
            SearchState.Loading,
        )

        viewModelScope.launch {
            tracksInteractor.searchTracks(searchText).collect(::processTracksData)
        }
    }

    private fun processTracksData(data: TracksData<List<Track>>) = renderHistoryIfVisibleOrRun {
        when (data) {
            is TracksData.Data -> {
                val tracks = data.value
                if (tracks.isEmpty()) {
                    renderState(
                        SearchState.EmptyResult,
                    )
                } else {
                    renderState(
                        SearchState.SearchResult(tracks),
                    )
                }
            }

            is TracksData.Error -> {
                renderState(
                    SearchState.Error,
                )
                showToast(data.error.code.toString())
            }
        }
    }

    private fun renderHistoryIfVisibleOrRun(fallbackRunner: (() -> Unit)?) {
        if (latestSearchText.isNullOrEmpty()) {
            renderState(SearchState.Empty)
            if (searchInputHasFocus == true) {
                viewModelScope.launch {
                    searchHistoryInteractor.getTracks().collect { tracks ->
                        if (tracks.isNotEmpty()) {
                            renderState(SearchState.History(tracks))
                        }
                    }
                }
            }
        } else {
            fallbackRunner?.invoke()
        }
    }

    private fun renderState(state: SearchState) = _stateLiveData.postValue(state)

    private fun showToast(message: String) {
        if (message.isNotEmpty()) {
            _showToast.postValue(message)
        }
    }

    fun addTrackToHistory(track: Track) {
        viewModelScope.launch {
            searchHistoryInteractor.addTrack(track)
            renderHistoryIfVisibleOrRun(null)
        }
    }

    fun clearHistory() {
        searchHistoryInteractor.clearTracks()
        renderHistoryIfVisibleOrRun(null)
    }

    companion object {
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
    }
}