package com.akalugin.playlistmaker.data.mapper

import com.akalugin.playlistmaker.data.dto.TrackDto
import com.akalugin.playlistmaker.domain.formatter.Formatter
import com.akalugin.playlistmaker.domain.models.Track

object TracksMapper {
    private const val YEAR_LENGTH = 4
    fun map(trackDto: TrackDto) = Track(
        trackId = trackDto.trackId,
        trackName = trackDto.trackName,
        artistName = trackDto.artistName,
        trackTime = Formatter.formatMilliseconds(trackDto.trackTimeMillis),
        artworkUrl100 = trackDto.artworkUrl100,
        collectionName = trackDto.collectionName,
        releaseYear = getReleaseYear(trackDto.releaseDate),
        primaryGenreName = trackDto.primaryGenreName,
        country = trackDto.country,
        previewUrl = trackDto.previewUrl,
    )

    private fun getReleaseYear(releaseDate: String) = if (releaseDate.length > YEAR_LENGTH)
        releaseDate.substring(0, YEAR_LENGTH)
    else releaseDate
}