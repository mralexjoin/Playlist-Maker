package com.akalugin.playlistmaker.data.db.playlists.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index

@Entity(
    tableName = "playlists_tracks",
    primaryKeys = ["playlistId", "trackId"],
    foreignKeys = [
        ForeignKey(
            onDelete = ForeignKey.CASCADE,
            entity = PlaylistEntity::class,
            parentColumns = ["playlistId"],
            childColumns = ["playlistId"],
        ),
        ForeignKey(
            onDelete = ForeignKey.CASCADE,
            entity = TrackEntity::class,
            parentColumns = ["trackId"],
            childColumns = ["trackId"],
        )
    ],
    indices = [Index("playlistId"), Index("trackId")]
)
data class PlaylistTrackCrossRef(
    val playlistId: Int,
    val trackId: Int,
)