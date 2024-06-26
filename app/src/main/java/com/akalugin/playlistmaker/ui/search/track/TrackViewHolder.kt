package com.akalugin.playlistmaker.ui.search.track

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.akalugin.playlistmaker.R
import com.akalugin.playlistmaker.databinding.TrackViewBinding
import com.akalugin.playlistmaker.domain.track.models.Track
import com.akalugin.playlistmaker.ui.utils.Utils
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners

class TrackViewHolder(
    parentView: ViewGroup,
    val binding: TrackViewBinding = TrackViewBinding.inflate(
        LayoutInflater.from(parentView.context), parentView, false
    )
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(model: Track) {
        with(binding) {
            trackNameTextView.text = model.trackName
            artistNameTextView.text = model.artistName
            trackTimeTextView.text = model.trackTime

            val context = itemView.context
            val artworkCornerRadiusPx =
                Utils.dpToPx(
                    context.resources.getDimension(R.dimen.track_view_artwork_corner_radius),
                    context
                )

            Glide.with(itemView)
                .load(model.artworkUrl100)
                .placeholder(R.drawable.album_placeholder)
                .fitCenter()
                .transform(RoundedCorners(artworkCornerRadiusPx))
                .into(artworkImageView)
        }
    }
}