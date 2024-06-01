package com.akalugin.playlistmaker.ui.search.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.akalugin.playlistmaker.databinding.FragmentSearchBinding
import com.akalugin.playlistmaker.ui.search.models.SearchState
import com.akalugin.playlistmaker.ui.search.track.TrackAdapter
import com.akalugin.playlistmaker.ui.search.view_model.SearchViewModel
import com.akalugin.playlistmaker.ui.utils.ClickDebounce
import com.akalugin.playlistmaker.ui.utils.Utils
import org.koin.androidx.viewmodel.ext.android.viewModel

class SearchFragment : Fragment() {
    private var _binding: FragmentSearchBinding? = null
    private val binding: FragmentSearchBinding
        get() = _binding!!

    private val viewModel: SearchViewModel by viewModel()

    private var inputMethodManager: InputMethodManager? = null

    private val trackAdapter = TrackAdapter()

    private val clickDebounce = ClickDebounce(lifecycleScope, CLICK_DEBOUNCE_DELAY_MILLIS)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        inputMethodManager =
            requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager

        with(binding) {
            clearSearchButton.setOnClickListener {
                searchInputEditText.apply {
                    text.clear()

                    inputMethodManager?.hideSoftInputFromWindow(windowToken, 0)
                }
            }

            val context = this@SearchFragment.requireContext()
            searchResultsRecyclerView.apply {
                layoutManager = LinearLayoutManager(context)
                adapter = trackAdapter
            }
            searchHistoryRecyclerView.apply {
                layoutManager = LinearLayoutManager(context)
                adapter = trackAdapter
            }

            val owner = this@SearchFragment.viewLifecycleOwner
            with(viewModel) {
                stateLiveData.observe(owner, ::render)
                clearButtonVisibilityLiveData.observe(
                    owner,
                    ::updateClearButtonVisibility
                )
                showToast.observe(
                    owner,
                    ::showToast
                )

                searchInputEditText.setOnEditorActionListener { _, actionId, _ ->
                    if (actionId == EditorInfo.IME_ACTION_DONE) {
                        searchTracks(searchInputEditText.text.toString())
                        inputMethodManager?.hideSoftInputFromWindow(
                            searchInputEditText.windowToken,
                            0
                        )
                        true
                    } else {
                        false
                    }
                }

                searchInputEditText.doOnTextChanged { text, _, _, _ ->
                    onSearchInputChanged(text.toString())
                }

                searchInputEditText.setOnFocusChangeListener { _, hasFocus ->
                    onSearchInputFocusChanged(hasFocus)
                }

                updateSearchResultsButton.setOnClickListener {
                    clickDebounce.debounce {
                        searchTracks(searchInputEditText.toString())
                    }
                }

                clearSearchHistoryButton.setOnClickListener {
                    clearHistory()
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

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
                    clickDebounce.debounce {
                        viewModel.addTrackToHistory(track)
                        Utils.playTrack(this@SearchFragment, track)
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
                    clickDebounce.debounce {
                        Utils.playTrack(this@SearchFragment, track)
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
        Toast.makeText(requireActivity().applicationContext, message, Toast.LENGTH_LONG).show()
    }

    private companion object {
        const val CLICK_DEBOUNCE_DELAY_MILLIS = 1_000L
    }
}