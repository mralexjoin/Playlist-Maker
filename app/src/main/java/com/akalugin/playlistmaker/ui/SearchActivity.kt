package com.akalugin.playlistmaker.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import androidx.recyclerview.widget.LinearLayoutManager
import com.akalugin.playlistmaker.Creator
import com.akalugin.playlistmaker.databinding.ActivitySearchBinding
import com.akalugin.playlistmaker.domain.api.search_history.SearchHistoryInteractor
import com.akalugin.playlistmaker.domain.consumer.Consumer
import com.akalugin.playlistmaker.domain.consumer.ConsumerData
import com.akalugin.playlistmaker.domain.models.Track
import com.akalugin.playlistmaker.ui.track.TrackAdapter

class SearchActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySearchBinding

    private var searchText: String? = null

    private var inputMethodManager: InputMethodManager? = null

    private var mainThreadHandler: Handler? = null
    private val searchRunnable = Runnable { searchTracks() }

    private val tracksInteractor = Creator.provideTracksInteractor()
    private var updateTracksRunnable: Runnable? = null

    private val searchResultsAdapter = TrackAdapter()
    private val searchHistoryAdapter = TrackAdapter()

    private var isClickAllowed = true

    private val searchHistoryInteractor: SearchHistoryInteractor by lazy {
        Creator.provideSearchHistoryInteractor(this.applicationContext)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
        mainThreadHandler = Handler(Looper.getMainLooper())

        binding.toolbar.setNavigationOnClickListener { finish() }

        initSearchInput()
        initSearchResults()
        initSearchHistory()
    }

    override fun onDestroy() {
        super.onDestroy()
        mainThreadHandler?.removeCallbacksAndMessages(null)
    }

    private fun initSearchInput() = binding.apply {
        searchInputEditText.doOnTextChanged { text, _, _, _ ->
            searchText = text.toString()
            if (searchText.isNullOrEmpty()) {
                clearSearchResults()
            }
            updateClearButtonVisibility()
            updateSearchHistoryVisibility()

            searchDebounce()
        }

        clearSearchButton.setOnClickListener {
            searchInputEditText.apply {
                text.clear()

                inputMethodManager?.hideSoftInputFromWindow(windowToken, 0)
            }

            mainThreadHandler?.removeCallbacks(searchRunnable)
            clearSearchResults()
        }

        searchText = searchInputEditText.text.toString()
        updateClearButtonVisibility()

        searchInputEditText.setOnFocusChangeListener { _, _ ->
            updateSearchHistoryVisibility()
        }
    }

    private fun initSearchResults() {
        searchResultsAdapter.onClickListener = TrackAdapter.OnClickListener { track ->
            if (clickDebounce()) {
                searchHistoryInteractor.addTrack(track)
                playTrack(track)
            }
        }

        binding.apply {
            searchResultsRecyclerView.apply {
                layoutManager = LinearLayoutManager(this@SearchActivity)
                adapter = searchResultsAdapter
            }

            updateSearchResultsButton.setOnClickListener { searchTracks() }
        }
    }

    private fun initSearchHistory() {
        searchHistoryAdapter.onClickListener = TrackAdapter.OnClickListener { track ->
            if (clickDebounce()) {
                playTrack(track)
            }
        }

        binding.searchHistoryRecyclerView.apply {
            layoutManager = LinearLayoutManager(this@SearchActivity)
            adapter = searchHistoryAdapter
        }
        searchHistoryInteractor.onItemsChangedListener =
            object : SearchHistoryInteractor.OnItemsChangedListener {
                override fun onItemsChanged(tracks: List<Track>) {
                    searchHistoryAdapter.setItems(tracks)
                    updateSearchHistoryVisibility()
                }
            }

        binding.clearSearchHistoryButton.setOnClickListener {
            searchHistoryInteractor.clearTracks()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        outState.putString(SEARCH_TEXT, searchText)

        val searchInputEditText = binding.searchInputEditText
        outState.putInt(SEARCH_SELECTION_START, searchInputEditText.selectionStart)
        outState.putInt(SEARCH_SELECTION_END, searchInputEditText.selectionEnd)

        outState.putBoolean(
            SEARCH_INPUT_ACTIVE, inputMethodManager?.isActive(searchInputEditText) ?: false
        )
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)

        val searchInputEditText = binding.searchInputEditText
        searchText = savedInstanceState.getString(SEARCH_TEXT, "")
        searchInputEditText.setText(searchText)

        val selectionStart = savedInstanceState.getInt(SEARCH_SELECTION_START, 0)
        val selectionEnd = savedInstanceState.getInt(SEARCH_SELECTION_END, 0)
        searchInputEditText.setSelection(selectionStart, selectionEnd)

        if (savedInstanceState.getBoolean(SEARCH_INPUT_ACTIVE, false)) {
            inputMethodManager?.showSoftInput(searchInputEditText, InputMethodManager.SHOW_IMPLICIT)
        }
    }

    private fun updateClearButtonVisibility() {
        binding.clearSearchButton.isVisible = !searchText.isNullOrEmpty()
    }

    private fun updateSearchHistoryVisibility() = binding.apply {
        searchHistoryLayout.isVisible = searchInputEditText.hasFocus()
                && searchText.isNullOrEmpty()
                && searchHistoryAdapter.itemCount > 0
    }

    private fun searchDebounce() = mainThreadHandler?.apply {
        removeCallbacks(searchRunnable)
        postDelayed(searchRunnable, SEARCH_DEBOUNCE_DELAY_MILLIS)
    }

    private fun searchTracks() {
        binding.apply {
            networkErrorLayout.isVisible = false
            nothingFoundTextView.isVisible = false
        }

        val text = searchText
        if (!text.isNullOrEmpty()) {

            binding.apply {
                searchResultsRecyclerView.isVisible = false
                searchProgressBar.isVisible = true
            }

            tracksInteractor.searchTracks(text, object : Consumer<List<Track>> {
                override fun consume(data: ConsumerData<List<Track>>) {
                    val currentRunnable = updateTracksRunnable
                    if (currentRunnable != null) {
                        mainThreadHandler?.removeCallbacks(currentRunnable)
                    }

                    val newpUpdateTracksRunnable = Runnable {
                        binding.searchProgressBar.isVisible = false
                        when (data) {
                            is ConsumerData.Data -> {
                                val tracks = data.value
                                if (tracks.isEmpty()) {
                                    binding.nothingFoundTextView.isVisible = true
                                } else {
                                    binding.searchResultsRecyclerView.isVisible = true
                                    binding.nothingFoundTextView.isVisible = false
                                    searchResultsAdapter.setItems(tracks)
                                }
                            }

                            is ConsumerData.Error -> {
                                showNetworkError(data.message)
                            }
                        }
                    }
                    updateTracksRunnable = newpUpdateTracksRunnable
                    mainThreadHandler?.post(newpUpdateTracksRunnable)
                }
            })
        }
    }

    private fun clearSearchResults() =
        searchResultsAdapter.setItems(emptyList())

    fun showNetworkError(additionalMessage: String) {
        binding.apply {
            networkErrorLayout.isVisible = true
            nothingFoundTextView.isVisible = false
        }

        if (additionalMessage.isNotEmpty()) {
            Toast.makeText(applicationContext, additionalMessage, Toast.LENGTH_LONG).show()
        }
    }

    private fun playTrack(track: Track) {
        val intent = Intent(this, AudioPlayerActivity::class.java).apply {
            putExtra(AudioPlayerActivity.TRACK_KEY_EXTRA, track)
        }
        startActivity(intent)
    }

    private fun clickDebounce() = isClickAllowed.also {
        if (isClickAllowed) {
            isClickAllowed = false
            mainThreadHandler?.postDelayed({ isClickAllowed = true }, CLICK_DEBOUNCE_DELAY_MILLIS)
        }
    }

    private companion object {
        const val SEARCH_TEXT = "SEARCH_TEXT"
        const val SEARCH_SELECTION_START = "SEARCH_SELECTION_START"
        const val SEARCH_SELECTION_END = "SEARCH_SELECTION_END"
        const val SEARCH_INPUT_ACTIVE = "SEARCH_INPUT_ACTIVE"
        const val SEARCH_DEBOUNCE_DELAY_MILLIS = 2_000L
        const val CLICK_DEBOUNCE_DELAY_MILLIS = 1_000L
    }
}