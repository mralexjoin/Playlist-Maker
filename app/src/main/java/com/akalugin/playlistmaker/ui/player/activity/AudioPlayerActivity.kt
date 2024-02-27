package com.akalugin.playlistmaker.ui.player.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.akalugin.playlistmaker.R
import com.akalugin.playlistmaker.databinding.ActivityAudioPlayerBinding
import com.akalugin.playlistmaker.domain.search.models.Track
import com.akalugin.playlistmaker.ui.player.models.AudioPlayerScreenState
import com.akalugin.playlistmaker.ui.player.view_model.AudioPlayerViewModel
import com.akalugin.playlistmaker.ui.utils.Utils.dpToPx
import com.akalugin.playlistmaker.ui.utils.Utils.serializable
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class AudioPlayerActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAudioPlayerBinding
    private var previewUrl: String? = null
    private val viewModel: AudioPlayerViewModel by viewModel {
        parametersOf(previewUrl)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAudioPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val track = intent.serializable<Track>(TRACK_KEY_EXTRA)!!
        previewUrl = track.previewUrl
        initTrackFields(track)

        with(viewModel) {
            binding.playButton.setOnClickListener {
                playbackControl()
            }
            audioPlayerScreenStateLiveData.observe(this@AudioPlayerActivity, ::render)
        }
    }

    override fun onPause() {
        super.onPause()
        viewModel.pause()
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.release()
    }

    private fun initTrackFields(track: Track) {
        val artworkCornerRadiusPx =
            dpToPx(resources.getDimension(R.dimen.player_artwork_corner_radius), this)
        Glide.with(this)
            .load(track.bigArtworkUrl)
            .placeholder(R.drawable.album_placeholder)
            .fitCenter()
            .transform(RoundedCorners(artworkCornerRadiusPx))
            .into(binding.artworkImageView)

        with(binding) {
            toolbar.setNavigationOnClickListener { finish() }

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

    private fun render(state: AudioPlayerScreenState) = with(binding) {
        playButton.isEnabled =
            (state is AudioPlayerScreenState.Playing) || (state is AudioPlayerScreenState.Paused)
        when (state) {
            is AudioPlayerScreenState.NoPreviewAvailable -> {
                currentPositionTextView.text = getString(R.string.no_preview_message)
            }

            is AudioPlayerScreenState.Loading -> {
                currentPositionTextView.text = getString(R.string.preview_is_loading_message)
            }

            is AudioPlayerScreenState.Playing -> {
                playButton.setImageResource(R.drawable.pause)
                currentPositionTextView.text = state.currentPosition
            }

            is AudioPlayerScreenState.Paused -> {
                playButton.setImageResource(R.drawable.play)
                currentPositionTextView.text = state.currentPosition
            }
        }
    }

    companion object {
        const val TRACK_KEY_EXTRA = "track"
    }
}
