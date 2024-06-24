package com.akalugin.playlistmaker.data.db.playlists.mapper

import com.akalugin.playlistmaker.data.db.playlists.entity.PlaylistEntity
import com.akalugin.playlistmaker.data.db.playlists.entity.PlaylistEntityWithTrackCount
import com.akalugin.playlistmaker.data.db.playlists.entity.PlaylistWithTracks
import com.akalugin.playlistmaker.data.db.playlists.entity.TrackEntity
import com.akalugin.playlistmaker.domain.playlists.models.Playlist
import com.akalugin.playlistmaker.domain.track.models.Track

object PlaylistMapper {
    fun mapToPlaylist(playlistEntityWithTrackCount: PlaylistEntityWithTrackCount) =
        with(playlistEntityWithTrackCount) {
            Playlist(
                playlistId,
                name,
                imagePath,
                description,
                trackCount = trackCount,
            )
        }

    fun mapToPlaylist(playlistWithTracks: PlaylistWithTracks) = with(playlistWithTracks) {
        Playlist(
            playlistEntity.playlistId,
            playlistEntity.name,
            playlistEntity.imagePath,
            playlistEntity.description,
            tracks.map(::mapToTrack),
        )
    }

    private fun mapToTrack(trackEntity: TrackEntity) = with(trackEntity) {
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
            bigArtworkUrl
        )
    }

    fun mapToPlaylistEntity(playlist: Playlist) = with(playlist) {
        PlaylistEntity(
            playlistId,
            name,
            imagePath,
            description,
        )
    }

    fun mapToTrackEntity(track: Track) = with(track) {
        TrackEntity(
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
            bigArtworkUrl
        )
    }
}