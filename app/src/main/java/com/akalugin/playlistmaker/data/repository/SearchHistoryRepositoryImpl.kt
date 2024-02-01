package com.akalugin.playlistmaker.data.repository

import android.content.Context
import com.akalugin.playlistmaker.domain.api.search_history.SearchHistoryRepository
import com.akalugin.playlistmaker.domain.models.Track
import com.akalugin.playlistmaker.domain.settings.Settings.PLAYLIST_MAKER_PREFERENCES
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class SearchHistoryRepositoryImpl(context: Context) : SearchHistoryRepository {
    private val sharedPreferences =
        context.getSharedPreferences(PLAYLIST_MAKER_PREFERENCES, Context.MODE_PRIVATE)
    private val gson = Gson()
    private val listOfTracksType = object : TypeToken<List<Track>>() {}.type

    override var onItemsChangedListener: SearchHistoryRepository.OnItemsChangedListener? = null
        set(value) {
            field = value
            onItemsChangedListener?.onItemsChanged(load())
        }

    override fun add(track: Track) {
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

        onItemsChangedListener?.onItemsChanged(tracks)
    }

    private fun load(): List<Track> {
        val json = sharedPreferences.getString(KEY_SEARCH_HISTORY, null) ?: return emptyList()
        return gson.fromJson(json, listOfTracksType)
    }

    private companion object {
        const val KEY_SEARCH_HISTORY = "search_history"
        const val MAX_SIZE = 10
    }

}