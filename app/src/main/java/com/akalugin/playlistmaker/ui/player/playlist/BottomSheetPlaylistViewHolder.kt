package com.akalugin.playlistmaker.ui.player.playlist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.akalugin.playlistmaker.R
import com.akalugin.playlistmaker.databinding.SmallPlaylistViewBinding
import com.akalugin.playlistmaker.domain.playlists.models.Playlist
import com.akalugin.playlistmaker.ui.library.playlists.utils.PlaylistUtils
import com.akalugin.playlistmaker.ui.utils.Utils
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners

class BottomSheetPlaylistViewHolder(
    parentView: ViewGroup,
    val binding: SmallPlaylistViewBinding = SmallPlaylistViewBinding.inflate(
        LayoutInflater.from(parentView.context), parentView, false
    )
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(model: Playlist) {
        with(binding) {
            val context = itemView.context

            smallPlaylistNameTextView.text = model.name
            val trackCount = model.trackCount
            smallPlaylistTrackCountTextView.text =
                PlaylistUtils.getTrackCount(context.resources, trackCount)

            val imageCornerRadiusPx =
                Utils.dpToPx(
                    context.resources.getDimension(R.dimen.track_view_artwork_corner_radius),
                    context
                )

            Glide.with(itemView)
                .load(model.imagePath)
                .placeholder(R.drawable.album_placeholder)
                .transform(CenterCrop(), RoundedCorners(imageCornerRadiusPx))
                .into(smallPlaylistImageView)
        }
    }
}
