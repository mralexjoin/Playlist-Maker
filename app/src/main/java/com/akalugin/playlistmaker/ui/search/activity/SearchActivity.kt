package com.akalugin.playlistmaker.ui.search.activity

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
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.akalugin.playlistmaker.creator.Creator
import com.akalugin.playlistmaker.databinding.ActivitySearchBinding
import com.akalugin.playlistmaker.domain.search.models.Track
import com.akalugin.playlistmaker.ui.player.activity.AudioPlayerActivity
import com.akalugin.playlistmaker.ui.search.models.SearchState
import com.akalugin.playlistmaker.ui.search.track.TrackAdapter
import com.akalugin.playlistmaker.ui.search.view_model.SearchViewModel
import com.akalugin.playlistmaker.ui.utils.Utils.serializable
import java.io.Serializable

class SearchActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySearchBinding
    private lateinit var viewModel: SearchViewModel

    private val mainThreadHandler = Handler(Looper.getMainLooper())

    private var inputMethodManager: InputMethodManager? = null

    private val trackAdapter = TrackAdapter()

    private var isClickAllowed = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.toolbar.setNavigationOnClickListener { finish() }

        inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager

        viewModel = ViewModelProvider(
            this,
            SearchViewModel.getVewModelFactory(
                Creator.provideTracksInteractor(),
                Creator.provideSearchHistoryInteractor(applicationContext),
            )
        )[SearchViewModel::class.java]

        with(binding) {

            viewModel.stateLiveData.observe(this@SearchActivity, ::render)
            viewModel.clearButtonVisibilityLiveData.observe(
                this@SearchActivity,
                ::updateClearButtonVisibility
            )
            viewModel.showToast.observe(
                this@SearchActivity,
                ::showToast
            )

            searchInputEditText.doOnTextChanged { text, _, _, _ ->
                viewModel.onSearchInputChanged(text.toString())
            }

            clearSearchButton.setOnClickListener {
                searchInputEditText.apply {
                    text.clear()

                    inputMethodManager?.hideSoftInputFromWindow(windowToken, 0)
                }
            }

            searchInputEditText.setOnFocusChangeListener { _, hasFocus ->
                viewModel.onSearchInputFocusChanged(hasFocus)
            }

            searchResultsRecyclerView.apply {
                layoutManager = LinearLayoutManager(this@SearchActivity)
                adapter = trackAdapter
            }
            searchHistoryRecyclerView.apply {
                layoutManager = LinearLayoutManager(this@SearchActivity)
                adapter = trackAdapter
            }

            updateSearchResultsButton.setOnClickListener {
                if (clickDebounce()) {
                    viewModel.searchTracks(searchInputEditText.text.toString())
                }
            }

            clearSearchHistoryButton.setOnClickListener {
                viewModel.clearHistory()
            }

            viewModel.onSearchInputChanged(searchInputEditText.text.toString())
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.onDestroy()
    }


    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        with(binding.searchInputEditText) {
            outState.putSerializable(
                INPUT_STATE_KEY,
                InputInstanceState(
                    text.toString(),
                    selectionStart,
                    selectionEnd,
                    inputMethodManager?.isActive(this) ?: false,
                )
            )
        }
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)

        savedInstanceState.serializable<InputInstanceState>(INPUT_STATE_KEY)?.let { state ->
            with(binding.searchInputEditText) {
                setText(state.text)
                setSelection(state.selectionStart, state.selectionEnd)
                inputMethodManager?.showSoftInput(this, InputMethodManager.SHOW_IMPLICIT)
            }
        }
    }

    data class InputInstanceState(
        val text: String,
        val selectionStart: Int,
        val selectionEnd: Int,
        val isInputActive: Boolean,
    ) : Serializable

    private fun updateClearButtonVisibility(isVisible: Boolean) {
        binding.clearSearchButton.isVisible = isVisible
    }

    private fun render(state: SearchState) = with(binding) {
        searchResultsRecyclerView.isVisible = false
        nothingFoundTextView.isVisible = false
        networkErrorLayout.isVisible = false
        searchHistoryLayout.isVisible = false
        searchProgressBar.isVisible = false

        when (state) {
            is SearchState.SearchResult -> {
                searchResultsRecyclerView.isVisible = true
                trackAdapter.setItems(state.tracks)
                trackAdapter.onClickListener = TrackAdapter.OnClickListener { track ->
                    if (clickDebounce()) {
                        viewModel.addTrackToHistory(track)
                        playTrack(track)
                    }
                }
            }

            is SearchState.EmptyResult -> {
                nothingFoundTextView.isVisible = true
            }

            is SearchState.History -> {
                searchHistoryLayout.isVisible = true
                trackAdapter.setItems(state.tracks)
                trackAdapter.onClickListener = TrackAdapter.OnClickListener { track ->
                    if (clickDebounce()) {
                        playTrack(track)
                    }
                }
            }

            is SearchState.Error -> {
                networkErrorLayout.isVisible = true
            }

            is SearchState.Loading -> {
                searchProgressBar.isVisible = true
            }

            else -> {}
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(applicationContext, message, Toast.LENGTH_LONG).show()
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
            mainThreadHandler.postDelayed({ isClickAllowed = true }, CLICK_DEBOUNCE_DELAY_MILLIS)
        }
    }

    private companion object {
        private const val INPUT_STATE_KEY = "INPUT_STATE"
        const val CLICK_DEBOUNCE_DELAY_MILLIS = 1_000L
    }
}