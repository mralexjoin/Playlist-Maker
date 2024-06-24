package com.akalugin.playlistmaker.data.search.mapper

import com.akalugin.playlistmaker.data.search.dto.TrackDto
import com.akalugin.playlistmaker.domain.formatter.Formatter
import com.akalugin.playlistmaker.domain.track.models.Track

object TrackMapper {
    private const val YEAR_LENGTH = 4
    private const val BIG_ARTWORK_URL_SUFFIX = "512x512bb.jpg"
    fun map(trackDto: TrackDto) = Track(
        trackId = trackDto.trackId ?: 0,
        trackName = trackDto.trackName ?: "No track name",
        artistName = trackDto.artistName ?: "No artist name",
        trackTime = Formatter.formatMilliseconds(trackDto.trackTimeMillis ?: 0),
        artworkUrl100 = trackDto.artworkUrl100 ?: "",
        collectionName = trackDto.collectionName ?: "",
        releaseYear = getReleaseYear(trackDto.releaseDate ?: ""),
        primaryGenreName = trackDto.primaryGenreName ?: "",
        country = trackDto.country ?: "",
        previewUrl = trackDto.previewUrl ?: "",
        bigArtworkUrl = bigArtworkUrl(trackDto.artworkUrl100 ?: ""),
    )

    private fun getReleaseYear(releaseDate: String) =
        if (releaseDate.length > YEAR_LENGTH)
            releaseDate.substring(0, YEAR_LENGTH)
        else releaseDate

    private fun bigArtworkUrl(smallArtworkUrl: String) =
        smallArtworkUrl.replaceAfterLast('/', BIG_ARTWORK_URL_SUFFIX)
}