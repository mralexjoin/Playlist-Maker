package com.akalugin.playlistmaker.data.search.mapper

import com.akalugin.playlistmaker.data.search.dto.TrackDto
import com.akalugin.playlistmaker.domain.formatter.Formatter
import com.akalugin.playlistmaker.domain.track.models.Track

object TrackMapper {
    private const val YEAR_LENGTH = 4
    private const val BIG_ARTWORK_URL_SUFFIX = "512x512bb.jpg"
    fun map(trackDto: TrackDto) = with(trackDto) {
        Track(
            trackId = trackId ?: 0,
            trackName = trackName ?: "No track name",
            artistName = artistName ?: "No artist name",
            trackTimeMillis = trackTimeMillis ?: 0,
            trackTime = Formatter.formatMilliseconds(trackTimeMillis ?: 0),
            artworkUrl100 = artworkUrl100 ?: "",
            collectionName = collectionName ?: "",
            releaseYear = getReleaseYear(releaseDate ?: ""),
            primaryGenreName = primaryGenreName ?: "",
            country = country ?: "",
            previewUrl = previewUrl ?: "",
            bigArtworkUrl = bigArtworkUrl(artworkUrl100 ?: ""),
        )
    }

    private fun getReleaseYear(releaseDate: String) =
        if (releaseDate.length > YEAR_LENGTH)
            releaseDate.substring(0, YEAR_LENGTH)
        else releaseDate

    private fun bigArtworkUrl(smallArtworkUrl: String) =
        smallArtworkUrl.replaceAfterLast('/', BIG_ARTWORK_URL_SUFFIX)
}