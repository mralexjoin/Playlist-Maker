package com.akalugin.playlistmaker.ui.player.playlist

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.akalugin.playlistmaker.domain.playlists.models.Playlist

class BottomSheetPlaylistAdapter : RecyclerView.Adapter<BottomSheetPlaylistViewHolder>() {
    private var playlists = listOf<Playlist>()
    var onClickListener: OnClickListener? = null

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BottomSheetPlaylistViewHolder {
        return BottomSheetPlaylistViewHolder(parent)
    }

    override fun getItemCount() = playlists.size

    override fun onBindViewHolder(holder: BottomSheetPlaylistViewHolder, position: Int) {
        val playlist = playlists[position]
        holder.bind(playlist)
        holder.binding.playlistLayout.setOnClickListener {
            onClickListener?.onClick(playlist)
        }
    }

    fun setItems(newPlaylists: List<Playlist>) {
        val oldPlaylists = playlists
        val diffUtil = object : DiffUtil.Callback() {
            override fun getOldListSize() = oldPlaylists.size

            override fun getNewListSize() = newPlaylists.size

            override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int) =
                oldPlaylists[oldItemPosition].playlistId == newPlaylists[newItemPosition].playlistId

            override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int) =
                oldPlaylists[oldItemPosition] == newPlaylists[newItemPosition]
        }
        val diffResult = DiffUtil.calculateDiff(diffUtil)
        playlists = newPlaylists
        diffResult.dispatchUpdatesTo(this)
    }

    fun interface OnClickListener {
        fun onClick(playlist: Playlist)
    }
}