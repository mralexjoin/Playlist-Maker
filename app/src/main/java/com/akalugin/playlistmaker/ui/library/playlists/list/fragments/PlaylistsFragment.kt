package com.akalugin.playlistmaker.ui.library.playlists.list.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.akalugin.playlistmaker.R
import com.akalugin.playlistmaker.databinding.FragmentPlaylistsBinding
import com.akalugin.playlistmaker.domain.playlists.models.Playlist
import com.akalugin.playlistmaker.ui.library.playlists.list.models.PlaylistScreenState
import com.akalugin.playlistmaker.ui.library.playlists.list.playlist.PlaylistAdapter
import com.akalugin.playlistmaker.ui.library.playlists.list.view_model.PlaylistsViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlaylistsFragment : Fragment() {
    private var _binding: FragmentPlaylistsBinding? = null
    private val binding: FragmentPlaylistsBinding
        get() = _binding!!
    private val viewModel: PlaylistsViewModel by viewModel()

    private val playlistAdapter = PlaylistAdapter()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPlaylistsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {
            newPlaylistButton.setOnClickListener {
                findNavController().navigate(R.id.action_libraryFragment_to_newPlaylistFragment)
            }

            recyclerView.apply {
                layoutManager = GridLayoutManager(context, COLUMN_COUNT)
                adapter = playlistAdapter
            }

            viewModel.state.observe(this@PlaylistsFragment.viewLifecycleOwner, ::render)
        }
    }

    override fun onStart() {
        super.onStart()

        viewModel.updatePlaylists()
    }

    private fun render(playlistScreenState: PlaylistScreenState?) {
        if (playlistScreenState == null)
            return

        when(playlistScreenState) {
            is PlaylistScreenState.Loading -> renderLoading()
            is PlaylistScreenState.Content -> renderContent(playlistScreenState.playlists)
            is PlaylistScreenState.Empty -> renderEmpty()
        }
    }

    private fun renderLoading() {
        with(binding) {
            progressBar.isVisible = true
            recyclerView.isVisible = false
            noPlaylistsTextView.isVisible = false
        }
    }

    private fun renderContent(playlists: List<Playlist>) {
        playlistAdapter.setItems(playlists)

        with(binding) {
            progressBar.isVisible = false
            recyclerView.isVisible = true
            noPlaylistsTextView.isVisible = false
        }
    }

    private fun renderEmpty() {
        with(binding) {
            progressBar.isVisible = false
            recyclerView.isVisible = false
            noPlaylistsTextView.isVisible = true
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        fun newInstance() = PlaylistsFragment()

        private const val COLUMN_COUNT = 2
    }
}