package com.akalugin.playlistmaker.data.db.playlists.mapper

import com.akalugin.playlistmaker.data.db.playlists.entity.PlaylistEntity
import com.akalugin.playlistmaker.data.db.playlists.entity.PlaylistEntityWithTrackCount
import com.akalugin.playlistmaker.domain.playlists.models.Playlist

object PlaylistMapper {
    fun map(playlist: Playlist) = with(playlist) {
        PlaylistEntity(
            playlistId,
            name,
            imagePath,
            description,
        )
    }

    fun map(playlistEntityWithTrackCount: PlaylistEntityWithTrackCount) =
        with(playlistEntityWithTrackCount) {
            Playlist(
                playlistId,
                name,
                imagePath,
                description,
                trackCount,
            )
        }
}