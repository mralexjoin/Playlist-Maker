package com.akalugin.playlistmaker.ui.search.track

import android.annotation.SuppressLint
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.akalugin.playlistmaker.domain.track.models.Track

class TrackAdapter : RecyclerView.Adapter<TrackViewHolder>() {
    private var tracks = listOf<Track>()
    var onClickListener: OnClickListener? = null
    var onLongClickListener: OnLongClickListener? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        return TrackViewHolder(parent)
    }

    override fun getItemCount() = tracks.size

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        val track = tracks[position]
        holder.bind(track)
        holder.binding.trackLayout.apply {
            setOnClickListener {
                onClickListener?.onClick(track)
            }
            setOnLongClickListener {
                onLongClickListener?.onClick(track)
                true
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setItems(newTracks: List<Track>) {
        tracks = newTracks
        notifyDataSetChanged()
    }

    fun setItemsWithDiff(newTracks: List<Track>) {
        val oldPlaylists = tracks
        val diffUtil = object : DiffUtil.Callback() {
            override fun getOldListSize() = oldPlaylists.size

            override fun getNewListSize() = newTracks.size

            override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int) =
                oldPlaylists[oldItemPosition].trackId == newTracks[newItemPosition].trackId

            override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int) =
                oldPlaylists[oldItemPosition] == newTracks[newItemPosition]
        }
        val diffResult = DiffUtil.calculateDiff(diffUtil)
        tracks = newTracks
        diffResult.dispatchUpdatesTo(this)
    }

    fun interface OnClickListener {
        fun onClick(track: Track)
    }

    fun interface OnLongClickListener {
        fun onClick(track: Track)
    }
}