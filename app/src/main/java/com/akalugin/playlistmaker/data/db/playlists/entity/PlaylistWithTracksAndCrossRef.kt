package com.akalugin.playlistmaker.data.db.playlists.entity

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation

data class PlaylistWithTracksAndCrossRef(
    @Embedded val playlistEntity: PlaylistEntity,
    @Relation(
        parentColumn = "playlistId",
        entityColumn = "trackId",
        associateBy = Junction(PlaylistTrackCrossRef::class)
    )
    val tracks: List<TrackEntity>,
    @Relation(
        entity = PlaylistTrackCrossRef::class,
        parentColumn = "playlistId",
        entityColumn = "playlistId",
    )
    val refs: List<PlaylistTrackCrossRef>,
)
