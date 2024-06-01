package com.akalugin.playlistmaker.ui.player.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.akalugin.playlistmaker.R
import com.akalugin.playlistmaker.databinding.ActivityAudioPlayerBinding
import com.akalugin.playlistmaker.domain.track.models.Track
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
    private var track: Track? = null
    private val viewModel: AudioPlayerViewModel by viewModel {
        parametersOf(track)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAudioPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.toolbar.setNavigationOnClickListener { finish() }

        track = intent.serializable<Track>(TRACK_KEY_EXTRA)
        initTrackFields(track)

        with(viewModel) {
            binding.playButton.setOnClickListener {
                playbackControl()
            }
            binding.addToFavoritesButton.setOnClickListener {
                onFavoriteClicked()
            }
            audioPlayerScreenStateLiveData.observe(this@AudioPlayerActivity, ::render)
        }
    }

    override fun onPause() {
        super.onPause()
        viewModel.pause()
    }

    private fun initTrackFields(track: Track?) {
        val artworkCornerRadiusPx =
            dpToPx(resources.getDimension(R.dimen.big_image_corner_radius), this)
        Glide.with(this)
            .load(track?.bigArtworkUrl)
            .placeholder(R.drawable.album_placeholder)
            .fitCenter()
            .transform(RoundedCorners(artworkCornerRadiusPx))
            .into(binding.artworkImageView)

        with(binding) {
            toolbar.setNavigationOnClickListener { finish() }

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
    }

    companion object {
        const val TRACK_KEY_EXTRA = "track"
    }
}
