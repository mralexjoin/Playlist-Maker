package com.akalugin.playlistmaker.ui.library.playlists.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import com.akalugin.playlistmaker.databinding.FragmentNewPlaylistBinding
import com.akalugin.playlistmaker.ui.library.playlists.view_model.NewPlaylistViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

/**
 * A simple [Fragment] subclass.
 * Use the [NewPlaylistFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class NewPlaylistFragment : Fragment() {
    private var _binding: FragmentNewPlaylistBinding? = null
    private val binding: FragmentNewPlaylistBinding
        get() = _binding!!
    private val viewModel: NewPlaylistViewModel by viewModel()
    private val pickMedia =
        registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            viewModel.saveImageToPrivateStorage(uri)
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

        binding.addPlaylistImageButton.setOnClickListener {
            pickMedia.launch(
                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
            )
        }
    }

    companion object {
        fun newInstance() = NewPlaylistFragment()
    }
}