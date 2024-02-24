package com.akalugin.playlistmaker.data.search.filter

import com.akalugin.playlistmaker.data.search.dto.TrackDto

object TracksFilter {
    fun filter(trackDto: TrackDto) = trackDto.trackId != null
}