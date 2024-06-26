package com.akalugin.playlistmaker.data.db.playlists.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.akalugin.playlistmaker.data.db.playlists.entity.PlaylistEntity
import com.akalugin.playlistmaker.data.db.playlists.entity.PlaylistEntityWithTrackCount
import com.akalugin.playlistmaker.data.db.playlists.entity.PlaylistTrackCrossRef
import com.akalugin.playlistmaker.data.db.playlists.entity.PlaylistWithTracks
import com.akalugin.playlistmaker.data.db.playlists.entity.PlaylistWithTracksAndCrossRef
import com.akalugin.playlistmaker.data.db.playlists.entity.TrackEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

@Dao
interface PlaylistsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPlaylist(playlistEntity: PlaylistEntity)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updatePlaylist(playlistEntity: PlaylistEntity)

    @Query(
        "SELECT playlists.*, COUNT(playlists_tracks.trackId) AS trackCount " +
                "FROM playlists AS playlists " +
                "LEFT JOIN playlists_tracks AS playlists_tracks " +
                "ON playlists.playlistId = playlists_tracks.playlistId " +
                "GROUP BY playlists.playlistId"
    )
    fun getPlaylistsWithTrackCount(): Flow<List<PlaylistEntityWithTrackCount>>

    @Transaction
    @Query("SELECT * FROM playlists")
    fun getPlaylistsWithTracks(): Flow<List<PlaylistWithTracks>>

    fun getPlaylistWithTracks(playlistId: Int): Flow<PlaylistWithTracks?> =
        getPlaylistWithTracksAndCrossRefs(playlistId).map {
            it?.run {
                val sortedRefs = refs.sortedByDescending { ref -> ref.timestamp }
                PlaylistWithTracks(
                    playlistEntity,
                    tracks.sortedBy { track ->
                        sortedRefs.indexOfFirst { ref -> ref.trackId == track.trackId }
                    }
                )
            }
        }

    @Transaction
    @Query("SELECT * FROM playlists WHERE playlistId = :playlistId")
    fun getPlaylistWithTracksAndCrossRefs(playlistId: Int): Flow<PlaylistWithTracksAndCrossRef?>

    @Transaction
    suspend fun addTrackToPlaylist(trackEntity: TrackEntity, playlistId: Int) {
        insertTrack(trackEntity)
        insertPlaylistTrackCrossRef(PlaylistTrackCrossRef(playlistId, trackEntity.trackId))
    }

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertTrack(trackEntity: TrackEntity)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertPlaylistTrackCrossRef(playlistTrackCrossRef: PlaylistTrackCrossRef)

    @Transaction
    suspend fun deleteTrackFromPlaylist(playlistTrackCrossRef: PlaylistTrackCrossRef) {
        deletePlaylistTrackCrossRef(playlistTrackCrossRef)
        deleteOrphanTracks(listOf(playlistTrackCrossRef.trackId))
    }

    @Transaction
    suspend fun deletePlaylistWithOrphanTracks(playlistWithTracks: PlaylistWithTracks) {
        deletePlaylist(playlistWithTracks.playlistEntity)
        deleteOrphanTracks(playlistWithTracks.tracks.map { trackEntity -> trackEntity.trackId })
    }

    @Delete
    suspend fun deletePlaylist(playlistEntity: PlaylistEntity)

    @Delete
    suspend fun deletePlaylistTrackCrossRef(playlistTrackCrossRef: PlaylistTrackCrossRef)

    @Query(
        "DELETE FROM tracks WHERE trackId IN (" +
                "SELECT tracks.trackId " +
                "FROM tracks " +
                "LEFT JOIN playlists_tracks " +
                "ON tracks.trackId = playlists_tracks.trackId " +
                "WHERE tracks.trackId IN (:trackIds) AND playlists_tracks.trackId IS NULL" +
                ")"
    )
    suspend fun deleteOrphanTracks(trackIds: List<Int>)
}