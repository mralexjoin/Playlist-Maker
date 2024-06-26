package com.akalugin.playlistmaker.ui.player.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.akalugin.playlistmaker.R
import com.akalugin.playlistmaker.databinding.FragmentAudioPlayerBinding
import com.akalugin.playlistmaker.domain.track.models.Track
import com.akalugin.playlistmaker.ui.player.models.AudioPlayerScreenState
import com.akalugin.playlistmaker.ui.player.models.BottomSheetPlaylistsState
import com.akalugin.playlistmaker.ui.player.playlist.BottomSheetPlaylistAdapter
import com.akalugin.playlistmaker.ui.player.view_model.AudioPlayerViewModel
import com.akalugin.playlistmaker.ui.utils.Utils
import com.akalugin.playlistmaker.ui.utils.Utils.serializable
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.android.material.bottomsheet.BottomSheetBehavior
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class AudioPlayerFragment : Fragment() {
    private var _binding: FragmentAudioPlayerBinding? = null
    private val binding: FragmentAudioPlayerBinding
        get() = _binding!!

    private var track: Track? = null
    private val viewModel: AudioPlayerViewModel by viewModel {
        parametersOf(track)
    }

    private val bottomSheetPlaylistAdapter = BottomSheetPlaylistAdapter()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAudioPlayerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        track = arguments?.serializable<Track>(TRACK_KEY_EXTRA)
        initTrackFields(track)

        with(viewModel) {
            with(binding) {
                playButton.setOnClickListener {
                    playbackControl()
                }
                addToFavoritesButton.setOnClickListener {
                    onFavoriteClicked()
                }
                addToPlaylistButton.setOnClickListener {
                    onAddToPlaylistButtonClicked()
                }
            }
            audioPlayerScreenStateLiveData.observe(
                viewLifecycleOwner,
                ::render
            )

            bottomSheetState.observe(
                viewLifecycleOwner,
                ::renderBottomSheet
            )

            showToast.observe(
                viewLifecycleOwner,
                ::showToast
            )
        }

        setupBottomSheet()
    }

    override fun onPause() {
        super.onPause()

        viewModel.pause()
    }

    override fun onResume() {
        super.onResume()

        viewModel.updatePlaylists()
    }

    private fun setupBottomSheet() {
        with(binding) {
            BottomSheetBehavior.from(playlistsBottomSheet).apply {
                state = BottomSheetBehavior.STATE_HIDDEN

                addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
                    override fun onStateChanged(bottomSheet: View, newState: Int) {
                        overlay.isVisible = newState != BottomSheetBehavior.STATE_HIDDEN
                    }

                    override fun onSlide(bottomSheet: View, slideOffset: Float) {
                        overlay.alpha = slideOffset
                    }
                })
            }

            newPlaylistButton.setOnClickListener {
                findNavController().navigate(R.id.action_audioPlayerFragment_to_newPlaylistFragment)
            }

            bottomSheetPlaylistAdapter.onClickListener =
                BottomSheetPlaylistAdapter.OnClickListener { playlist ->
                    viewModel.addTrackToPlaylist(track, playlist)
                }

            playlistsRecyclerView.apply {
                layoutManager = LinearLayoutManager(context)
                adapter = bottomSheetPlaylistAdapter
            }
        }
    }

    private fun initTrackFields(track: Track?) {
        context?.let {
            val artworkCornerRadiusPx =
                Utils.dpToPx(resources.getDimension(R.dimen.big_image_corner_radius), it)
            Glide.with(it)
                .load(track?.bigArtworkUrl)
                .placeholder(R.drawable.album_placeholder)
                .fitCenter()
                .transform(RoundedCorners(artworkCornerRadiusPx))
                .into(binding.artworkImageView)
        }

        with(binding) {
            track?.let { track ->
                trackNameTextView.text = track.trackName
                artistNameTextView.text = track.artistName
                trackTimeTextView.text = track.trackTime

                albumFieldGroup.isVisible = track.collectionName.isNotEmpty()
                albumTextView.text = track.collectionName

                yearTextView.text = track.releaseYear
                genreTextView.text = track.primaryGenreName
                countryTextView.text = track.country
            }
        }
    }

    private fun render(state: AudioPlayerScreenState) = with(binding) {
        playButton.isEnabled = state.playerState == AudioPlayerScreenState.PlayerState.READY
        currentPositionTextView.text = when (state.playerState) {
            AudioPlayerScreenState.PlayerState.READY -> state.currentPosition
            AudioPlayerScreenState.PlayerState.LOADING -> getString(R.string.preview_is_loading_message)
            AudioPlayerScreenState.PlayerState.NO_PREVIEW_AVAILABLE -> getString(R.string.no_preview_message)
        }
        playButton.setImageResource(
            if (state.isPlaying) R.drawable.pause else R.drawable.play
        )
        addToFavoritesButton.setImageResource(
            if (state.isFavorite) R.drawable.remove_from_favorites else R.drawable.add_to_favorites
        )
        BottomSheetBehavior.from(playlistsBottomSheet).state = if (state.isBottomSheetVisible)
            BottomSheetBehavior.STATE_HALF_EXPANDED
        else
            BottomSheetBehavior.STATE_HIDDEN
    }

    private fun renderBottomSheet(bottomSheetPlaylistsState: BottomSheetPlaylistsState) {
        with(binding) {
            when (bottomSheetPlaylistsState) {
                is BottomSheetPlaylistsState.Loading -> {
                    progressBar.isVisible = true
                    playlistsRecyclerView.isVisible = false
                }

                is BottomSheetPlaylistsState.Content -> {
                    progressBar.isVisible = false
                    playlistsRecyclerView.isVisible = true

                    bottomSheetPlaylistAdapter.setItems(bottomSheetPlaylistsState.playlists)
                }
            }
        }
    }

    private fun showToast(toastData: Pair<Int, Array<Any>>) {
        Toast.makeText(
            requireActivity().applicationContext,
            getString(toastData.first, *toastData.second),
            Toast.LENGTH_LONG
        ).show()
    }

    companion object {
        const val TRACK_KEY_EXTRA = "track"
    }
}