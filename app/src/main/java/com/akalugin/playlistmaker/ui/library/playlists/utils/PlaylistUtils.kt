package com.akalugin.playlistmaker.ui.library.playlists.utils

import android.content.res.Resources
import com.akalugin.playlistmaker.R

object PlaylistUtils {
    fun getTrackCount(resources: Resources, trackCount: Int) =
        resources.getQuantityString(
            R.plurals.number_of_tracks,
            trackCount,
            trackCount
        )
}