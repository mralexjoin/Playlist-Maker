package com.akalugin.playlistmaker.data.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.akalugin.playlistmaker.data.db.entity.FavoriteTrackEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteTrackDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun add(track: FavoriteTrackEntity)

    @Delete
    suspend fun remove(track: FavoriteTrackEntity)

    @Query("SELECT * FROM favorite_tracks ORDER BY timestamp DESC")
    fun get(): Flow<List<FavoriteTrackEntity>>

    @Query("SELECT trackId FROM favorite_tracks WHERE trackId IN (:trackIds)")
    suspend fun filterIds(trackIds: List<Int>): List<Int>
}