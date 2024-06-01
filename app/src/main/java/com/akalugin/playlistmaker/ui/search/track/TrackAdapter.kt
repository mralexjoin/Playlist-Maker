package com.akalugin.playlistmaker.ui.search.track

import android.annotation.SuppressLint
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.akalugin.playlistmaker.domain.track.models.Track

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

    @SuppressLint("NotifyDataSetChanged")
    fun setItems(newTracks: List<Track>) {
        tracks = newTracks
        notifyDataSetChanged()
    }

    fun interface OnClickListener {
        fun onClick(track: Track)
    }
}