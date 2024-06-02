package com.akalugin.playlistmaker.ui.player.playlist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.akalugin.playlistmaker.R
import com.akalugin.playlistmaker.databinding.SmallPlaylistViewBinding
import com.akalugin.playlistmaker.domain.playlists.models.Playlist
import com.bumptech.glide.Glide

class BottomSheetPlaylistViewHolder(
    parentView: ViewGroup,
    val binding: SmallPlaylistViewBinding = SmallPlaylistViewBinding.inflate(
        LayoutInflater.from(parentView.context), parentView, false
    )
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(model: Playlist) {
        with(binding) {
            val context = itemView.context

            playlistNameTextView.text = model.name
            val trackCount = model.trackCount
            trackCountTextView.text =
                context.resources.getQuantityString(
                    R.plurals.number_of_tracks,
                    trackCount,
                    trackCount
                )

            Glide.with(itemView)
                .load(model.imagePath)
                .placeholder(R.drawable.album_placeholder)
                .fitCenter()
                .into(playlistImageView)
        }
    }
}
