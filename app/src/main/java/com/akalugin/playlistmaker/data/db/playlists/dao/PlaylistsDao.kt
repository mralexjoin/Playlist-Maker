package com.akalugin.playlistmaker.data.db.playlists.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import com.akalugin.playlistmaker.data.db.playlists.entity.PlaylistEntity

@Dao
interface PlaylistsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addPlaylist(playlistEntity: PlaylistEntity)


}