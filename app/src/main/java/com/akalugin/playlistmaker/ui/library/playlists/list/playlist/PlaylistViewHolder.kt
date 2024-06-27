package com.akalugin.playlistmaker.ui.library.playlists.list.playlist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.akalugin.playlistmaker.R
import com.akalugin.playlistmaker.databinding.BigPlaylistViewBinding
import com.akalugin.playlistmaker.domain.playlists.models.Playlist
import com.akalugin.playlistmaker.ui.library.playlists.utils.PlaylistUtils
import com.akalugin.playlistmaker.ui.utils.Utils
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners

class PlaylistViewHolder(
    parentView: ViewGroup,
    val binding: BigPlaylistViewBinding = BigPlaylistViewBinding.inflate(
        LayoutInflater.from(parentView.context), parentView, false
    )
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(model: Playlist) {
        with(binding) {
            val context = itemView.context

            playlistNameTextView.text = model.name
            trackCountTextView.text =
                PlaylistUtils.getTrackCount(context.resources, model.trackCount)

            val imageCornerRadiusPx = Utils.dpToPx(
                context.resources.getDimension(R.dimen.big_image_corner_radius),
                context
            )

            Glide.with(itemView)
                .load(model.imagePath)
                .placeholder(R.drawable.album_placeholder)
                .transform(CenterCrop(), RoundedCorners(imageCornerRadiusPx))
                .into(playlistImageView)
        }
    }
}
