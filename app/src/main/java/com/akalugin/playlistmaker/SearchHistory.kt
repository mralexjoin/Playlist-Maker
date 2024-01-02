package com.akalugin.playlistmaker

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class SearchHistory(context: Context) {
    private val sharedPreferences =
        context.getSharedPreferences(PLAYLIST_MAKER_PREFERENCES, Context.MODE_PRIVATE)
    private val gson = Gson()
    private val listOfTracksType = object : TypeToken<List<Track>>() {}.type

    var onItemsChangedListener: OnItemsChangedListener? = null
        set(value) {
            field = value
            onItemsChangedListener?.onItemsChanged(load())
        }

    fun add(track: Track) {
        val tracks: MutableList<Track> = load().toMutableList().apply {
            removeAll { it.trackId == track.trackId }
            if (size >= MAX_SIZE)
                removeLast()
            add(0, track)
        }

        save(tracks)
    }

    fun clear() {
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

    fun interface OnItemsChangedListener {
        fun onItemsChanged(tracks: List<Track>)
    }
}