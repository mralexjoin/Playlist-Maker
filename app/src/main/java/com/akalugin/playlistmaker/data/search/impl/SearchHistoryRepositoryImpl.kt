package com.akalugin.playlistmaker.data.search.impl

import android.content.SharedPreferences
import com.akalugin.playlistmaker.domain.favorites.FavoriteTracksRepository
import com.akalugin.playlistmaker.domain.search.history.SearchHistoryRepository
import com.akalugin.playlistmaker.domain.search.models.Track
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class SearchHistoryRepositoryImpl(
    private val sharedPreferences: SharedPreferences,
    private val gson: Gson,
    private val favoriteTracksRepository: FavoriteTracksRepository,
) : SearchHistoryRepository {
    private val listOfTracksType = object : TypeToken<List<Track>>() {}.type

    override suspend fun getHistory(): Flow<List<Track>> = flow {
        emit(load())
    }

    override suspend fun add(track: Track) {
        val tracks: MutableList<Track> = load().toMutableList().apply {
            removeAll { it.trackId == track.trackId }
            if (size >= MAX_SIZE)
                removeLast()
            add(0, track)
        }

        save(tracks)
    }

    override fun clear() {
        save(emptyList())
    }

    private fun save(tracks: List<Track>) {
        val json = gson.toJson(tracks)
        sharedPreferences.edit()
            .putString(KEY_SEARCH_HISTORY, json)
            .apply()
    }

    private suspend fun load(): List<Track> {
        val json = sharedPreferences.getString(KEY_SEARCH_HISTORY, null) ?: return emptyList()
        val tracks: List<Track> = gson.fromJson(json, listOfTracksType)
        favoriteTracksRepository.setIsFavorite(tracks)
        return tracks
    }

    private companion object {
        const val KEY_SEARCH_HISTORY = "search_history"
        const val MAX_SIZE = 10
    }

}