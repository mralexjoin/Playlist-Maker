package com.akalugin.playlistmaker

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.akalugin.playlistmaker.domain.settings.Settings.PLAYLIST_MAKER_PREFERENCES

class App : Application() {
    var darkTheme = false
        private set

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

    private companion object {
        const val KEY_DARK_THEME = "dark_theme"
    }
}