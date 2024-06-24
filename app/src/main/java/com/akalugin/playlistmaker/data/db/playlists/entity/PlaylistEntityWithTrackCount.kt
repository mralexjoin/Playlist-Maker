package com.akalugin.playlistmaker.data.db.playlists.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "playlists")
data class PlaylistEntityWithTrackCount(
    @PrimaryKey(autoGenerate = true)
    val playlistId: Int,
    val name: String,
    val imagePath: String?,
    val description: String?,
    val trackCount: Int,
)