package com.akalugin.playlistmaker.ui.library.playlists.playlist.framents

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.akalugin.playlistmaker.R
import com.akalugin.playlistmaker.databinding.FragmentPlaylistBinding
import com.akalugin.playlistmaker.domain.playlists.models.Playlist
import com.akalugin.playlistmaker.domain.track.models.Track
import com.akalugin.playlistmaker.ui.library.playlists.creation.fragments.EditPlaylistFragment
import com.akalugin.playlistmaker.ui.library.playlists.playlist.models.PlaylistScreenState
import com.akalugin.playlistmaker.ui.library.playlists.playlist.models.PlaylistSingleLiveEvent
import com.akalugin.playlistmaker.ui.library.playlists.playlist.view_model.PlaylistViewModel
import com.akalugin.playlistmaker.ui.library.playlists.utils.PlaylistUtils
import com.akalugin.playlistmaker.ui.root.RootActivity
import com.akalugin.playlistmaker.ui.search.track.TrackAdapter
import com.akalugin.playlistmaker.ui.utils.Utils
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import org.koin.androidx.viewmodel.ext.android.viewModel
import kotlin.math.round

class PlaylistFragment : Fragment() {
    private var _binding: FragmentPlaylistBinding? = null
    private val binding: FragmentPlaylistBinding
        get() = _binding!!
    private val viewModel: PlaylistViewModel by viewModel()

    private val trackAdapter = TrackAdapter()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPlaylistBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.state.observe(viewLifecycleOwner, ::render)
        viewModel.singleLiveEvent.observe(viewLifecycleOwner, ::processEvent)

        with(binding) {
            backImageButton.setOnClickListener {
                findNavController().popBackStack()
            }

            sharePlaylistImageButton.setOnClickListener {
                viewModel.sharePlaylist()
            }

            sharePlaylistTextView.setOnClickListener {
                viewModel.sharePlaylist()
            }

            editPlaylistTextView.setOnClickListener {
                viewModel.editPlaylist()
            }

            deletePlaylistTextView.setOnClickListener {
                showDeleteDialog()
            }

            morePlaylistImageButton.setOnClickListener {
                BottomSheetBehavior.from(moreActionsBottomSheet).state =
                    BottomSheetBehavior.STATE_HALF_EXPANDED
            }
        }

        setupBottomSheets()

        val playlistId = arguments?.getInt(PLAYLIST_ID_EXTRA)
        viewModel.loadPlaylist(playlistId)
    }


    private fun render(state: PlaylistScreenState?) {
        if (state == null)
            return

        renderPlaylist(state.playlist)
    }

    private fun renderPlaylist(playlist: Playlist) {
        with(binding) {
            with(playlist) {
                context?.let {
                    Glide.with(it)
                        .load(imagePath)
                        .placeholder(R.drawable.album_placeholder)
                        .centerCrop()
                        .into(playlistImageView)


                    val imageCornerRadiusPx =
                        Utils.dpToPx(
                            it.resources.getDimension(R.dimen.track_view_artwork_corner_radius),
                            it
                        )
                    Glide.with(it)
                        .load(imagePath)
                        .placeholder(R.drawable.album_placeholder)
                        .transform(CenterCrop(), RoundedCorners(imageCornerRadiusPx))
                        .into(smallPlaylistInfoLayout.smallPlaylistImageView)
                }

                val durationText = getDuration(tracks).let {
                    resources.getQuantityString(R.plurals.number_of_minutes, it, it)
                }
                val trackCountText = PlaylistUtils.getTrackCount(resources, trackCount)

                playlistNameTextView.text = name
                playlistDescriptionTextView.text = description
                playlistDurationTextView.text = durationText
                playlistTrackCountTextView.text = trackCountText

                trackAdapter.setItemsWithDiff(tracks)

                with(smallPlaylistInfoLayout) {
                    smallPlaylistNameTextView.text = name
                    smallPlaylistTrackCountTextView.text = trackCountText
                }
            }
        }
    }

    private fun getDuration(tracks: List<Track>) =
        round(1.0 * tracks.sumOf { it.trackTimeMillis } / MILLISECONDS_IN_MINUTE).toInt()

    private fun setupBottomSheets() {
        with(binding) {
            trackAdapter.apply {
                onClickListener = TrackAdapter.OnClickListener { track ->
                    Utils.playTrack(
                        findNavController(),
                        R.id.action_playlistFragment_to_audioPlayerFragment,
                        track,
                    )
                }

                onLongClickListener = TrackAdapter.OnLongClickListener { track ->
                    context?.let {
                        MaterialAlertDialogBuilder(it)
                            .setTitle(R.string.delete_track_dialog_header)
                            .setMessage(R.string.delete_track_dialog_message)
                            .setNegativeButton(R.string.delete_track_dialog_option_negative) { _, _ ->
                            }
                            .setPositiveButton(R.string.delete_track_dialog_option_positive) { _, _ ->
                                viewModel.deleteTrack(track)
                            }.show()
                    }
                }
            }

            playlistTracksRecyclerView.apply {
                layoutManager = LinearLayoutManager(context)
                adapter = trackAdapter
            }

            val bottomSheetCallback = object : BottomSheetBehavior.BottomSheetCallback() {
                override fun onStateChanged(bottomSheet: View, newState: Int) {
                    overlay.isVisible = newState != BottomSheetBehavior.STATE_HIDDEN
                }

                override fun onSlide(bottomSheet: View, slideOffset: Float) {
                    overlay.alpha = slideOffset
                }
            }

            BottomSheetBehavior.from(playlistTracksBottomSheet)
                .addBottomSheetCallback(bottomSheetCallback)
            BottomSheetBehavior.from(moreActionsBottomSheet).apply {
                state = BottomSheetBehavior.STATE_HIDDEN
                addBottomSheetCallback(bottomSheetCallback)
            }
        }
    }

    private fun processEvent(event: PlaylistSingleLiveEvent) {
        when (event) {
            is PlaylistSingleLiveEvent.ToastEvent -> processToastEvent(event)
            is PlaylistSingleLiveEvent.CloseEvent -> findNavController().popBackStack()
            is PlaylistSingleLiveEvent.EditPlaylistEvent -> findNavController().navigate(
                R.id.action_playlistFragment_to_newPlaylistFragment,
                bundleOf(EditPlaylistFragment.PLAYLIST_KEY_EXTRA to event.playlist)
            )
        }
    }

    private fun processToastEvent(event: PlaylistSingleLiveEvent.ToastEvent) {
        showToast(
            when (event) {
                is PlaylistSingleLiveEvent.ToastEvent.EmptyPlaylist -> {
                    getString(R.string.empty_playlist_share_message)
                }
            }
        )
    }

    private fun showDeleteDialog() {
        context?.let {
            MaterialAlertDialogBuilder(it)
                .setTitle(R.string.delete_playlist_dialog_header)
                .setMessage(R.string.delete_playlist_dialog_message)
                .setNegativeButton(R.string.no) { _, _ ->
                }
                .setPositiveButton(R.string.yes) { _, _ ->
                    viewModel.deletePlaylist()
                }.show()
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(
            requireActivity().applicationContext,
            message,
            Toast.LENGTH_LONG
        ).show()
    }

    override fun onStart() {
        super.onStart()
        (activity as? RootActivity)?.toolbar?.isVisible = false
    }

    override fun onStop() {
        super.onStop()
        (activity as? RootActivity)?.toolbar?.isVisible = true
    }

    companion object {
        const val PLAYLIST_ID_EXTRA = "playlist_id"
        private const val MILLISECONDS_IN_MINUTE = 60 * 1000
    }
}