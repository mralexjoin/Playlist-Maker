package com.akalugin.playlistmaker.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.akalugin.playlistmaker.data.db.favorites.dao.FavoriteTrackDao
import com.akalugin.playlistmaker.data.db.favorites.entity.FavoriteTrackEntity
import com.akalugin.playlistmaker.data.db.playlists.dao.PlaylistsDao
import com.akalugin.playlistmaker.data.db.playlists.entity.PlaylistEntity
import com.akalugin.playlistmaker.data.db.playlists.entity.PlaylistTrackCrossRef
import com.akalugin.playlistmaker.data.db.playlists.entity.TrackEntity

@Database(
    version = 1,
    entities = [
        FavoriteTrackEntity::class,
        PlaylistEntity::class,
        TrackEntity::class,
        PlaylistTrackCrossRef::class,
    ],
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun favoritesDao(): FavoriteTrackDao
    abstract fun playlistsDao(): PlaylistsDao
}