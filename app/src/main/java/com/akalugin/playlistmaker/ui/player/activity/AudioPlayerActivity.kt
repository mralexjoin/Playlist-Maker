package com.akalugin.playlistmaker.ui.player.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import com.akalugin.playlistmaker.R
import com.akalugin.playlistmaker.creator.Creator
import com.akalugin.playlistmaker.databinding.ActivityAudioPlayerBinding
import com.akalugin.playlistmaker.domain.search.models.Track
import com.akalugin.playlistmaker.ui.player.view_model.AudioPlayerViewModel
import com.akalugin.playlistmaker.ui.utils.Utils.dpToPx
import com.akalugin.playlistmaker.ui.utils.Utils.serializable
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners

class AudioPlayerActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAudioPlayerBinding
    private lateinit var viewModel: AudioPlayerViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAudioPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val track = intent.serializable<Track>(TRACK_KEY_EXTRA)!!
        initTrackFields(track)
        viewModel = ViewModelProvider(
            this@AudioPlayerActivity,
            AudioPlayerViewModel.getViewModelFactory(
                track.previewUrl,
                Creator.provideAudioPlayerInteractor(),
            )
        )[AudioPlayerViewModel::class.java]

        with(viewModel) {
            currentPositionLiveData.observe(this@AudioPlayerActivity) {
                binding.currentPositionTextView.text = it ?: getString(R.string.no_preview_message)
            }

            with(binding.playButton) {
                setOnClickListener {
                    playbackControl()
                }

                playbackButtonEnabledLiveData.observe(this@AudioPlayerActivity) {
                    isEnabled = it
                }

                playbackButtonDrawableLiveData.observe(this@AudioPlayerActivity) {
                    setImageResource(it)
                }
            }
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

            if (track.previewUrl.isEmpty()) {
                currentPositionTextView.text = getString(R.string.no_preview_message)
            }
        }
    }

    companion object {
        const val TRACK_KEY_EXTRA = "track"
    }
}
