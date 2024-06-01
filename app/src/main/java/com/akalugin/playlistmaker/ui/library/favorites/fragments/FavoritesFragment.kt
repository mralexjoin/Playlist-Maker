package com.akalugin.playlistmaker.ui.library.favorites.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.akalugin.playlistmaker.databinding.FragmentFavoritesBinding
import com.akalugin.playlistmaker.domain.search.models.Track
import com.akalugin.playlistmaker.ui.library.favorites.models.FavoritesState
import com.akalugin.playlistmaker.ui.library.favorites.view_model.FavoritesViewModel
import com.akalugin.playlistmaker.ui.search.track.TrackAdapter
import com.akalugin.playlistmaker.ui.utils.ClickDebounce
import com.akalugin.playlistmaker.ui.utils.Utils
import org.koin.androidx.viewmodel.ext.android.viewModel

class FavoritesFragment : Fragment() {
    private var _binding: FragmentFavoritesBinding? = null
    private val binding: FragmentFavoritesBinding
        get() = _binding!!

    private val viewModel: FavoritesViewModel by viewModel()
    private val trackAdapter = TrackAdapter()

    private val clickDebounce = ClickDebounce(lifecycleScope, CLICK_DEBOUNCE_DELAY_MILLIS)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoritesBinding.inflate(inflater, container, false)

        binding.favoritesRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = trackAdapter
        }
        trackAdapter.onClickListener = TrackAdapter.OnClickListener { track ->
            clickDebounce.debounce {
                Utils.playTrack(this@FavoritesFragment, track)
            }
        }

        with(viewModel) {
            stateLiveData.observe(viewLifecycleOwner, ::render)
        }

        return binding.root
    }

    override fun onStart() {
        super.onStart()

        viewModel.getFavorites()
    }

    private fun render(state: FavoritesState) {
        when (state) {
            is FavoritesState.Loading -> renderLoading()
            is FavoritesState.Empty -> renderEmpty()
            is FavoritesState.Content -> renderContent(state.tracks)
        }
    }

    private fun renderLoading() = with(binding) {
        getFavoritesProgressBar.isVisible = true
        emptyFavoritesTextView.isVisible = false
        favoritesRecyclerView.isVisible = false
    }

    private fun renderEmpty() = with(binding) {
        getFavoritesProgressBar.isVisible = false
        emptyFavoritesTextView.isVisible = true
        favoritesRecyclerView.isVisible = false
    }

    private fun renderContent(tracks: List<Track>) = with(binding) {
        getFavoritesProgressBar.isVisible = false
        emptyFavoritesTextView.isVisible = false
        favoritesRecyclerView.isVisible = true

        trackAdapter.setItems(tracks)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        fun newInstance() = FavoritesFragment()
        private const val CLICK_DEBOUNCE_DELAY_MILLIS = 1_000L
    }
}