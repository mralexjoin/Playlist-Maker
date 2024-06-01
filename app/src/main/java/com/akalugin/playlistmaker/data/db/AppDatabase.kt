package com.akalugin.playlistmaker.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.akalugin.playlistmaker.data.db.dao.FavoriteTrackDao
import com.akalugin.playlistmaker.data.db.entity.FavoriteTrackEntity

@Database(version = 1, entities = [FavoriteTrackEntity::class])
abstract class AppDatabase : RoomDatabase() {
    abstract fun trackDao(): FavoriteTrackDao
}