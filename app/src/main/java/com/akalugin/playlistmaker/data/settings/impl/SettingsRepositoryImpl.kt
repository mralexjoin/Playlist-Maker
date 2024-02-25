package com.akalugin.playlistmaker.data.settings.impl

import android.app.Application
import android.content.Context
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatDelegate
import com.akalugin.playlistmaker.App
import com.akalugin.playlistmaker.data.Constants
import com.akalugin.playlistmaker.domain.settings.SettingsRepository

class SettingsRepositoryImpl(context: Context) : SettingsRepository {
    private val app = context as App

    override var darkTheme: Boolean = getTheme()
        set(value) {
            field = value
            switchTheme(field)
        }

    private fun getTheme() =
        app.getSharedPreferences(Constants.PLAYLIST_MAKER_PREFERENCES, Application.MODE_PRIVATE)
            .let {
                if (it.contains(KEY_DARK_THEME))
                    it.getBoolean(KEY_DARK_THEME, false)
                else {
                    when (app.resources.configuration.uiMode.and(Configuration.UI_MODE_NIGHT_MASK)) {
                        Configuration.UI_MODE_NIGHT_YES -> true
                        else -> false
                    }
                }
            }

    override fun applyTheme() {
        switchTheme(darkTheme)
    }

    private fun switchTheme(darkThemeEnabled: Boolean) {
        AppCompatDelegate.setDefaultNightMode(
            if (darkThemeEnabled) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )

        app.getSharedPreferences(Constants.PLAYLIST_MAKER_PREFERENCES, Application.MODE_PRIVATE)
            .edit()
            .putBoolean(KEY_DARK_THEME, darkTheme)
            .apply()
    }

    private companion object {
        const val KEY_DARK_THEME = "dark_theme"
    }
}