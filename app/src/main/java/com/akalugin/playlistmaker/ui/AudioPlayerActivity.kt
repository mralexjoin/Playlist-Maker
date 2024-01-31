package com.akalugin.playlistmaker.ui

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.akalugin.playlistmaker.Creator
import com.akalugin.playlistmaker.R
import com.akalugin.playlistmaker.databinding.ActivityAudioPlayerBinding
import com.akalugin.playlistmaker.domain.api.AudioPlayer
import com.akalugin.playlistmaker.domain.formatter.Formatter
import com.akalugin.playlistmaker.domain.models.Track
import com.akalugin.playlistmaker.presentation.Utils.dpToPx
import com.akalugin.playlistmaker.presentation.Utils.serializable
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners

class AudioPlayerActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAudioPlayerBinding

    private val audioPlayerInteractor = Creator.provideAudioPlayerInteractor()
    private var mainThreadHandler: Handler? = null
    private val updateCurrentPositionRunnable = Runnable { updateCurrentPosition() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAudioPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mainThreadHandler = Handler(Looper.getMainLooper())

        intent.serializable<Track>(TRACK_KEY_EXTRA)!!.let {
            initTrackFields(it)
            initAudioPlayer(it.previewUrl)
        }
    }

    override fun onPause() {
        super.onPause()
        audioPlayerInteractor.pause()
    }

    override fun onDestroy() {
        super.onDestroy()
        audioPlayerInteractor.release()
        mainThreadHandler?.removeCallbacksAndMessages(null)
    }

    private fun initTrackFields(track: Track) {
        val artworkCornerRadiusPx =
            dpToPx(resources.getDimension(R.dimen.player_artwork_corner_radius), this)
        Glide.with(this)
            .load(bigArtworkUrl(track.artworkUrl100))
            .placeholder(R.drawable.album_placeholder)
            .fitCenter()
            .transform(RoundedCorners(artworkCornerRadiusPx))
            .into(binding.artworkImageView)

        binding.apply {
            toolbar.setNavigationOnClickListener { finish() }

            trackNameTextView.text = track.trackName
            artistNameTextView.text = track.artistName
            trackTimeTextView.text = track.trackTime

            albumFieldGroup.isVisible = !track.collectionName.isNullOrEmpty()
            albumTextView.text = track.collectionName

            yearTextView.text = track.releaseYear
            genreTextView.text = track.primaryGenreName
            countryTextView.text = track.country
        }
    }

    private fun bigArtworkUrl(smallArtworkUrl: String) =
        smallArtworkUrl.replaceAfterLast('/', BIG_ARTWORK_URL_SUFFIX)

    private fun initAudioPlayer(previewUrl: String) {
        val playButton = binding.playButton

        audioPlayerInteractor.apply {
            onStateChangedListener = object : AudioPlayer.OnStateChangedListener {
                override fun onStateChanged(state: AudioPlayer.State) {
                    if (state == AudioPlayer.State.PLAYING) {
                        playButton.setImageResource(R.drawable.pause)
                        updateCurrentPosition()
                    } else {
                        playButton.setImageResource(R.drawable.play)
                        stopUpdateCurrentPosition()

                        if (state == AudioPlayer.State.PREPARED) {
                            playButton.isEnabled = true
                            setCurrentPosition(0)
                        }
                    }
                }
            }
            prepare(previewUrl)
        }

        playButton.setOnClickListener {
            audioPlayerInteractor.playbackControl()
        }
    }

    private fun updateCurrentPosition() {
        setCurrentPosition(audioPlayerInteractor.currentPosition)
        mainThreadHandler?.postDelayed(updateCurrentPositionRunnable, UPDATE_PLAYER_ACTIVITY_DELAY_MILLIS)
    }

    private fun stopUpdateCurrentPosition() {
        mainThreadHandler?.removeCallbacks(updateCurrentPositionRunnable)
    }

    private fun setCurrentPosition(currentPosition: Int) {
        binding.currentPositionTextView.text = Formatter.formatMilliseconds(currentPosition)
    }

    companion object {
        private const val UPDATE_PLAYER_ACTIVITY_DELAY_MILLIS = 300L

        private const val BIG_ARTWORK_URL_SUFFIX = "512x512bb.jpg"
        const val TRACK_KEY_EXTRA = "track"
    }
}
