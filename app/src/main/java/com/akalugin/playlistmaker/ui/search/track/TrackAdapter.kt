package com.akalugin.playlistmaker.ui.search.track

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.akalugin.playlistmaker.domain.search.models.Track

class TrackAdapter : RecyclerView.Adapter<TrackViewHolder>() {
    private var tracks = listOf<Track>()
    var onClickListener: OnClickListener? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        return TrackViewHolder(parent)
    }

    override fun getItemCount() = tracks.size

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        val track = tracks[position]
        holder.bind(track)
        holder.binding.trackLayout.setOnClickListener {
            onClickListener?.onClick(track)
        }
    }

    fun setItems(newTracks: List<Track>) {
        val oldTracks = tracks
        val diffUtil = object : DiffUtil.Callback() {
            override fun getOldListSize() = oldTracks.size

            override fun getNewListSize() = newTracks.size

            override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int) =
                oldTracks[oldItemPosition].trackId == newTracks[newItemPosition].trackId

            override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int) =
                oldTracks[oldItemPosition] == newTracks[newItemPosition]
        }
        val diffResult = DiffUtil.calculateDiff(diffUtil)
        tracks = newTracks
        diffResult.dispatchUpdatesTo(this)
    }

    fun interface OnClickListener {
        fun onClick(track: Track)
    }
}