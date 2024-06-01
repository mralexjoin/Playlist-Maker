package com.akalugin.playlistmaker.data.db.mapper

import com.akalugin.playlistmaker.data.db.entity.FavoriteTrackEntity
import com.akalugin.playlistmaker.domain.search.models.Track

object FavoriteTrackMapper {
    fun map(trackEntity: FavoriteTrackEntity) = with(trackEntity) {
        Track(
            trackId,
            trackName,
            artistName,
            trackTime,
            artworkUrl100,
            collectionName,
            releaseYear,
            primaryGenreName,
            country,
            previewUrl,
            bigArtworkUrl,
            true
        )
    }

    fun map(track: Track) = with(track) {
        FavoriteTrackEntity(
            trackId,
            trackName,
            artistName,
            trackTime,
            artworkUrl100,
            collectionName,
            releaseYear,
            primaryGenreName,
            country,
            previewUrl,
            bigArtworkUrl,
        )
    }
}