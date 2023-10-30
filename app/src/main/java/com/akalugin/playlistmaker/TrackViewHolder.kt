package com.akalugin.playlistmaker

import android.content.Context
import android.util.TypedValue
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners

class TrackViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val trackNameTextView: TextView = itemView.findViewById(R.id.track_name)
    private val artistNameTextView: TextView = itemView.findViewById(R.id.artist_name)
    private val trackTimeTextView: TextView = itemView.findViewById(R.id.track_time)
    private val artworkImageView: ImageView = itemView.findViewById(R.id.artwork)

    fun bind(model: Track) {
        trackNameTextView.text = model.trackName
        artistNameTextView.text = model.artistName
        trackTimeTextView.text = model.trackTime

        val context = itemView.context
        val artworkCornerRadiusPx =
            dpToPx(context.resources.getDimension(R.dimen.artwork_corner_radius), context)

        Glide.with(itemView)
            .load(model.artworkUrl100)
            .placeholder(R.drawable.album_placeholder)
            .fitCenter()
            .transform(RoundedCorners(artworkCornerRadiusPx))
            .into(artworkImageView)
    }

    companion object {
        fun dpToPx(dp: Float, context: Context) = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP, dp, context.resources.displayMetrics
        ).toInt()
    }
}