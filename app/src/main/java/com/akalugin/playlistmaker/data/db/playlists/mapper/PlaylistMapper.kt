package com.akalugin.playlistmaker.data.db.playlists.mapper

import com.akalugin.playlistmaker.data.db.playlists.entity.PlaylistEntity
import com.akalugin.playlistmaker.data.db.playlists.entity.PlaylistWithTracks
import com.akalugin.playlistmaker.data.db.playlists.entity.TrackEntity
import com.akalugin.playlistmaker.domain.playlists.models.Playlist
import com.akalugin.playlistmaker.domain.track.models.Track

object PlaylistMapper {
    fun map(playlist: Playlist) = with(playlist) {
        PlaylistEntity(
            playlistId,
            name,
            imagePath,
            description,
        )
    }

    fun map(playlistWithTracks: PlaylistWithTracks) = with(playlistWithTracks) {
        Playlist(
            playlistEntity.id,
            playlistEntity.name,
            playlistEntity.imagePath,
            playlistEntity.description,
            tracks.map(PlaylistMapper::mapTrack),
        )
    }

    private fun mapTrack(trackEntity: TrackEntity) = with(trackEntity) {
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
        )
    }
}