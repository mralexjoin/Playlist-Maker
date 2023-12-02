package com.akalugin.playlistmaker

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate

const val PLAYLIST_MAKER_PREFERENCES = "playlist_maker_preferences"
const val KEY_DARK_THEME = "dark_theme"

class App : Application() {
    var darkTheme = false

    override fun onCreate() {
        super.onCreate()

        getSharedPreferences(PLAYLIST_MAKER_PREFERENCES, MODE_PRIVATE).let {
            if (it.contains(KEY_DARK_THEME))
                switchTheme(it.getBoolean(KEY_DARK_THEME, false))
        }
    }

    fun switchTheme(darkThemeEnabled: Boolean) {
        darkTheme = darkThemeEnabled
        AppCompatDelegate.setDefaultNightMode(
            if (darkThemeEnabled) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )

        getSharedPreferences(PLAYLIST_MAKER_PREFERENCES, MODE_PRIVATE).edit()
            .putBoolean(KEY_DARK_THEME, darkTheme)
            .apply()
    }
}