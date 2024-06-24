package com.akalugin.playlistmaker.ui.library.playlists.list.playlist

import android.annotation.SuppressLint
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.akalugin.playlistmaker.domain.playlists.models.Playlist

class PlaylistAdapter : RecyclerView.Adapter<PlaylistViewHolder>() {
    private var playlists = listOf<Playlist>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaylistViewHolder {
        return PlaylistViewHolder(parent)
    }
    override fun getItemCount() = playlists.size

    override fun onBindViewHolder(holder: PlaylistViewHolder, position: Int) {
        val playlist = playlists[position]
        holder.bind(playlist)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setItems(newPlaylists: List<Playlist>) {
        playlists = newPlaylists
        notifyDataSetChanged()
    }
}