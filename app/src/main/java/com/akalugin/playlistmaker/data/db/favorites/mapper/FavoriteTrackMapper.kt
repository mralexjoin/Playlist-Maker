package com.akalugin.playlistmaker.data.db.favorites.mapper

import com.akalugin.playlistmaker.data.db.favorites.entity.FavoriteTrackEntity
import com.akalugin.playlistmaker.domain.track.models.Track

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