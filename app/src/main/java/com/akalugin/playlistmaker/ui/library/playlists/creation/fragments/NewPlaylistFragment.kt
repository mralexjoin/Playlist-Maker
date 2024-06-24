package com.akalugin.playlistmaker.ui.library.playlists.creation.fragments

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
import com.akalugin.playlistmaker.ui.library.playlists.creation.models.NewPlaylistScreenState
import com.akalugin.playlistmaker.ui.library.playlists.creation.view_model.NewPlaylistViewModel
import com.akalugin.playlistmaker.ui.utils.Utils
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import org.koin.androidx.viewmodel.ext.android.viewModel

class NewPlaylistFragment : Fragment() {
    private var _binding: FragmentNewPlaylistBinding? = null
    private val binding: FragmentNewPlaylistBinding
        get() = _binding!!
    private val viewModel: NewPlaylistViewModel by viewModel()
    private val pickMedia =
        registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            viewModel.saveImageToPrivateStorage(uri)
        }
    private var onBackPressedCallback: OnBackPressedCallback? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNewPlaylistBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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
                        createPlaylist()
                    }
                }
            }
        }

        onBackPressedCallback = activity?.onBackPressedDispatcher?.let { dispatcher ->
            dispatcher.addCallback(this, false) {
                this@NewPlaylistFragment.context?.let {
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
    }

    private fun render(newPlaylistScreenState: NewPlaylistScreenState) =
        with(newPlaylistScreenState) {
            if (imageUri != null) {
                val playlistImageCornerRadiusPx =
                    Utils.dpToPx(
                        resources.getDimension(R.dimen.big_image_corner_radius),
                        this@NewPlaylistFragment.requireContext()
                    )
                Glide.with(this@NewPlaylistFragment)
                    .load(imageUri)
                    .placeholder(R.drawable.add_playlist_image)
                    .fitCenter()
                    .transform(RoundedCorners(playlistImageCornerRadiusPx))
                    .into(binding.addPlaylistImageButton)
            }
            binding.createPlaylistButton.isEnabled =
                playlistName.isNotEmpty()
            onBackPressedCallback?.isEnabled =
                imageUri != null || playlistName.isNotEmpty() || !description.isNullOrEmpty()
            if (close) {
                findNavController().popBackStack()
            }
        }

    private fun showToast(toastData: Pair<Int, Array<Any>>) {
        Toast.makeText(
            requireActivity().applicationContext,
            getString(toastData.first, *toastData.second),
            Toast.LENGTH_LONG
        ).show()
    }
}