package com.akalugin.playlistmaker.data.settings.impl

import android.content.SharedPreferences
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatDelegate
import com.akalugin.playlistmaker.App
import com.akalugin.playlistmaker.domain.settings.SettingsRepository

class SettingsRepositoryImpl(
    private val app: App,
    private val sharedPreferences: SharedPreferences,
) : SettingsRepository {

    override var darkTheme: Boolean = getTheme()
        set(value) {
            field = value
            switchTheme(field)
        }

    private fun getTheme() =
        if (sharedPreferences.contains(KEY_DARK_THEME)) {
            sharedPreferences.getBoolean(KEY_DARK_THEME, false)
        } else {
            val uiModeNight =
                app.resources.configuration.uiMode.and(Configuration.UI_MODE_NIGHT_MASK)
            uiModeNight == Configuration.UI_MODE_NIGHT_YES
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

        sharedPreferences
            .edit()
            .putBoolean(KEY_DARK_THEME, darkTheme)
            .apply()
    }

    private companion object {
        const val KEY_DARK_THEME = "dark_theme"
    }
}