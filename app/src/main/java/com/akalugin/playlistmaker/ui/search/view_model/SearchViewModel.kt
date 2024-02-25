package com.akalugin.playlistmaker.ui.search.view_model

import android.os.Handler
import android.os.Looper
import android.os.SystemClock
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.akalugin.playlistmaker.domain.search.consumer.Consumer
import com.akalugin.playlistmaker.domain.search.consumer.ConsumerData
import com.akalugin.playlistmaker.domain.search.history.SearchHistoryInteractor
import com.akalugin.playlistmaker.domain.search.models.Track
import com.akalugin.playlistmaker.domain.search.tracks.TracksInteractor
import com.akalugin.playlistmaker.ui.search.models.SearchState
import com.akalugin.playlistmaker.ui.utils.SingleLiveEvent

class SearchViewModel(
    private val tracksInteractor: TracksInteractor,
    private val searchHistoryInteractor: SearchHistoryInteractor,
) : ViewModel() {

    private val mainThreadHandler = Handler(Looper.getMainLooper())

    private var latestSearchText: String? = null

    private var tracksHistory: List<Track> = emptyList()

    private val mStateLiveData = MutableLiveData<SearchState>()
    val stateLiveData: LiveData<SearchState>
        get() = mStateLiveData

    private val mShowToast = SingleLiveEvent<String>()
    val showToast: LiveData<String>
        get() = mShowToast

    private var searchInputHasFocus: Boolean? = null
    private var isHistoryVisible = false

    private val mClearButtonVisibilityLiveData = MutableLiveData(false)
    val clearButtonVisibilityLiveData: LiveData<Boolean>
        get() = mClearButtonVisibilityLiveData

    init {
        searchHistoryInteractor.onItemsChangedListener =
            object : SearchHistoryInteractor.OnItemsChangedListener {
                override fun onItemsChanged(tracks: List<Track>) {
                    tracksHistory = tracks
                    renderHistoryIfVisible()
                }
            }
    }

    fun onDestroy() {
        mainThreadHandler.removeCallbacksAndMessages(null)
    }

    fun onSearchInputChanged(changedText: String) {
        if (latestSearchText == changedText)
            return
        latestSearchText = changedText
        updateClearButtonVisibility(changedText)
        updateHistoryVisibilityAndRender()

        searchDebounce(changedText)
    }

    fun onSearchInputFocusChanged(hasFocus: Boolean) {
        searchInputHasFocus = hasFocus
        updateHistoryVisibilityAndRender()
    }

    private fun updateClearButtonVisibility(changedText: String) {
        mClearButtonVisibilityLiveData.value = changedText.isNotEmpty()
    }

    private fun searchDebounce(changedText: String) {
        mainThreadHandler.removeCallbacksAndMessages(SEARCH_REQUEST_TOKEN)
        val searchRunnable = Runnable { searchTracks(changedText) }
        val postTime = SystemClock.uptimeMillis() + SEARCH_DEBOUNCE_DELAY
        mainThreadHandler.postAtTime(searchRunnable, SEARCH_REQUEST_TOKEN, postTime)
    }

    fun searchTracks(searchText: String) {
        if (searchText.isNotEmpty()) {
            renderState(
                SearchState.Loading,
            )

            tracksInteractor.searchTracks(searchText, object : Consumer<List<Track>> {
                override fun consume(data: ConsumerData<List<Track>>) =
                    synchronized(HISTORY_VISIBILITY_SYNC) {
                        if (isHistoryVisible) {
                            renderHistory()
                        } else {
                            when (data) {
                                is ConsumerData.Data -> {
                                    val tracks = data.value
                                    if (tracks.isEmpty()) {
                                        renderState(
                                            SearchState.EmptyResult
                                        )
                                    } else {
                                        renderState(
                                            SearchState.SearchResult(tracks)
                                        )
                                    }
                                }

                                is ConsumerData.Error -> {
                                    renderState(
                                        SearchState.Error,
                                    )
                                    showToast(data.message)
                                }
                            }
                        }
                    }
            })
        }
    }

    private fun updateHistoryVisibilityAndRender() {
        isHistoryVisible =
            (latestSearchText.isNullOrEmpty() && (searchInputHasFocus == true))
        renderHistoryIfVisible()
    }

    private fun renderHistoryIfVisible() {
        if (isHistoryVisible) {
            synchronized(HISTORY_VISIBILITY_SYNC) {
                if (isHistoryVisible) {
                    renderHistory()
                }
            }
        }
    }

    private fun renderHistory() {
        mainThreadHandler.removeCallbacksAndMessages(SEARCH_REQUEST_TOKEN)
        renderState(
            if (tracksHistory.isEmpty())
                SearchState.Empty
            else
                SearchState.History(tracksHistory)
        )
    }

    private fun renderState(state: SearchState) = mStateLiveData.postValue(state)

    private fun showToast(message: String) {
        if (message.isNotEmpty()) {
            mShowToast.postValue(message)
        }
    }

    fun addTrackToHistory(track: Track) = searchHistoryInteractor.addTrack(track)

    fun clearHistory() = searchHistoryInteractor.clearTracks()

    companion object {
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
        private val SEARCH_REQUEST_TOKEN = Any()
        private val HISTORY_VISIBILITY_SYNC = Any()

        fun getVewModelFactory(
            tracksInteractor: TracksInteractor,
            searchHistoryInteractor: SearchHistoryInteractor,
        ): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                SearchViewModel(
                    tracksInteractor,
                    searchHistoryInteractor,
                )
            }
        }
    }
}