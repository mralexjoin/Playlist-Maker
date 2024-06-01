package com.akalugin.playlistmaker.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite_tracks")
data class FavoriteTrackEntity (
    @PrimaryKey
    val trackId: Int,
    val trackName: String,
    val artistName: String,
    val trackTime: String,
    val artworkUrl100: String,
    val collectionName: String,
    val releaseYear: String,
    val primaryGenreName: String,
    val country: String,
    val previewUrl: String,
    val bigArtworkUrl: String,
    val timestamp: Long = System.currentTimeMillis()
)
