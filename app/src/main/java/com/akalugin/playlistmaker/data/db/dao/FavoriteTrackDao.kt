package com.akalugin.playlistmaker.data.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.akalugin.playlistmaker.data.db.entity.FavoriteTrackEntity

@Dao
interface FavoriteTrackDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun add(track: FavoriteTrackEntity)

    @Delete
    suspend fun remove(track: FavoriteTrackEntity)

    @Query("SELECT * FROM favorite_tracks ORDER BY timestamp DESC")
    suspend fun get(): List<FavoriteTrackEntity>

    @Query("SELECT trackId FROM favorite_tracks WHERE trackId IN (:trackIds)")
    suspend fun filterIds(trackIds: List<Int>): List<Int>

    @Query("SELECT trackId FROM favorite_tracks WHERE trackId IN (:trackId)")
    suspend fun isFavorite(trackId: Int): Int
}