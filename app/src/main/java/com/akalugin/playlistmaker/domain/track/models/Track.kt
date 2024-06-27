package com.akalugin.playlistmaker.domain.track.models

import java.io.Serializable
import java.util.Objects

data class Track(
    val trackId: Int,
    val trackName: String,
    val artistName: String,
    val trackTimeMillis: Int,
    val trackTime: String,
    val artworkUrl100: String,
    val collectionName: String,
    val releaseYear: String,
    val primaryGenreName: String,
    val country: String,
    val previewUrl: String,
    val bigArtworkUrl: String,
    var isFavorite: Boolean = false,
) : Serializable {
    override fun equals(other: Any?): Boolean {
        if (other == null)
            return false
        if (other !is Track)
            return false
        return this.trackId == other.trackId
    }

    override fun hashCode() = Objects.hashCode(trackId)
}
