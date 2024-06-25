package com.akalugin.playlistmaker.ui.library.playlists.creation.fragments

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.addCallback
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.akalugin.playlistmaker.R
import com.akalugin.playlistmaker.databinding.FragmentNewPlaylistBinding
import com.akalugin.playlistmaker.domain.playlists.models.Playlist
import com.akalugin.playlistmaker.ui.library.playlists.creation.models.EditPlaylistScreenState
import com.akalugin.playlistmaker.ui.library.playlists.creation.view_model.EditPlaylistViewModel
import com.akalugin.playlistmaker.ui.root.RootActivity
import com.akalugin.playlistmaker.ui.utils.Utils
import com.akalugin.playlistmaker.ui.utils.Utils.serializable
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class EditPlaylistFragment : Fragment() {
    private var _binding: FragmentNewPlaylistBinding? = null
    private val binding: FragmentNewPlaylistBinding
        get() = _binding!!
    private val pickMedia =
        registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            viewModel.saveImageToPrivateStorage(uri)
        }
    private var onBackPressedCallback: OnBackPressedCallback? = null
    private var playlist: Playlist? = null
    private val viewModel: EditPlaylistViewModel by viewModel {
        parametersOf(playlist)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNewPlaylistBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        playlist = arguments?.serializable<Playlist>(PLAYLIST_KEY_EXTRA)

        with(viewModel) {

            stateLiveData.observe(viewLifecycleOwner, ::render)
            showToast.observe(viewLifecycleOwner, ::showToast)

            with(binding) {
                addPlaylistImageButton.setOnClickListener {
                    clickDebounce {
                        pickMedia.launch(
                            PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                        )
                    }
                }

                playlistNameEditText.doOnTextChanged { text, _, _, _ ->
                    onPlaylistNameChanged(text)
                }
                playlistDescriptionEditText.doOnTextChanged { text, _, _, _ ->
                    onPlaylistDescriptionChanged(text)
                }

                createPlaylistButton.setOnClickListener {
                    clickDebounce {
                        savePlaylist()
                    }
                }
            }
        }

        onBackPressedCallback = activity?.onBackPressedDispatcher?.let { dispatcher ->
            dispatcher.addCallback(this, false) {
                this@EditPlaylistFragment.context?.let {
                    MaterialAlertDialogBuilder(it)
                        .setTitle(R.string.new_playlist_close_confirm_dialog_title)
                        .setMessage(R.string.new_playlist_close_confirm_dialog_message)
                        .setNegativeButton(R.string.new_playlist_close_confirm_dialog_negative) { _, _ ->
                        }
                        .setPositiveButton(R.string.new_playlist_close_confirm_dialog_positive) { _, _ ->
                            isEnabled = false
                            dispatcher.onBackPressed()
                        }.show()
                }
            }
        }

        playlist?.apply {
            renderNameDescription(name, description)
        }
    }


    private fun render(editPlaylistScreenState: EditPlaylistScreenState) =
        with(editPlaylistScreenState) {
            if (close) {
                findNavController().popBackStack()
                return
            }

            renderImage(imageUri)
            renderTitles(operation)

            binding.createPlaylistButton.isEnabled = createPlaylistButtonEnabled
            onBackPressedCallback?.isEnabled = needsExitConfirmation
        }

    private fun renderTitles(operation: EditPlaylistScreenState.Operation) {
        val titles = when (operation) {
            EditPlaylistScreenState.Operation.CREATE ->
                R.string.new_playlist to R.string.create_playlist

            EditPlaylistScreenState.Operation.EDIT ->
                R.string.edit_playlist to R.string.save_playlist
        }

        (activity as? RootActivity)?.toolbar?.title = getText(titles.first)
        binding.createPlaylistButton.setText(titles.second)
    }

    private fun renderImage(imageUri: Uri?) {
        if (imageUri != null) {
            val playlistImageCornerRadiusPx =
                Utils.dpToPx(
                    resources.getDimension(R.dimen.big_image_corner_radius),
                    this@EditPlaylistFragment.requireContext()
                )
            Glide.with(this@EditPlaylistFragment)
                .load(imageUri)
                .placeholder(R.drawable.add_playlist_image)
                .transform(CenterCrop(), RoundedCorners(playlistImageCornerRadiusPx))
                .into(binding.addPlaylistImageButton)
        }
    }

    private fun renderNameDescription(playlistName: String, description: String?) {
        with(binding) {
            playlistNameEditText.setText(playlistName)
            playlistDescriptionEditText.setText(description)
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
        const val PLAYLIST_KEY_EXTRA = "playlist"
    }
}