package com.akalugin.playlistmaker.data.db.playlists.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.akalugin.playlistmaker.data.db.playlists.entity.PlaylistEntity
import com.akalugin.playlistmaker.data.db.playlists.entity.PlaylistEntityWithTrackCount
import com.akalugin.playlistmaker.data.db.playlists.entity.PlaylistWithTracks
import kotlinx.coroutines.flow.Flow

@Dao
interface PlaylistsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addPlaylist(playlistEntity: PlaylistEntity)


    @Query("SELECT playlists.*, COUNT(playlists_tracks.trackId) AS trackCount " +
            "FROM playlists AS playlists " +
            "LEFT JOIN playlists_tracks AS playlists_tracks " +
            "ON playlists.playlistId = playlists_tracks.playlistId " +
            "GROUP BY playlists.playlistId")
    fun getPlaylistsWithTrackCount(): Flow<List<PlaylistEntityWithTrackCount>>

    @Transaction
    @Query("SELECT * FROM playlists")
    fun getPlaylistsWithTracks(): Flow<List<PlaylistWithTracks>>
}