package com.akalugin.playlistmaker

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.akalugin.playlistmaker.databinding.ActivityAudioPlayerBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners

class AudioPlayerActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityAudioPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initTrackFields(binding, intent.serializable(TRACK_KEY_EXTRA)!!)
    }

    private fun initTrackFields(binding: ActivityAudioPlayerBinding, track: Track) {
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

    companion object {
        private const val BIG_ARTWORK_URL_SUFFIX = "512x512bb.jpg"
        const val TRACK_KEY_EXTRA = "track"
    }
}
