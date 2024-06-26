package com.akalugin.playlistmaker.ui.library.favorites.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.akalugin.playlistmaker.R
import com.akalugin.playlistmaker.databinding.FragmentFavoritesBinding
import com.akalugin.playlistmaker.domain.track.models.Track
import com.akalugin.playlistmaker.ui.library.favorites.models.FavoritesState
import com.akalugin.playlistmaker.ui.library.favorites.view_model.FavoritesViewModel
import com.akalugin.playlistmaker.ui.search.track.TrackAdapter
import com.akalugin.playlistmaker.ui.utils.Utils
import org.koin.androidx.viewmodel.ext.android.viewModel

class FavoritesFragment : Fragment() {
    private var _binding: FragmentFavoritesBinding? = null
    private val binding: FragmentFavoritesBinding
        get() = _binding!!

    private val viewModel: FavoritesViewModel by viewModel()
    private val trackAdapter = TrackAdapter()


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
            viewModel.clickDebounce {
                Utils.playTrack(
                    findNavController(),
                    R.id.action_libraryFragment_to_audioPlayerFragment,
                    track,
                )
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
    }
}