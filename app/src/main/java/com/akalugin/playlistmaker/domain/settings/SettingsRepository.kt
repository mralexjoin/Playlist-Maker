package com.akalugin.playlistmaker.domain.settings

interface SettingsRepository {
    var darkTheme: Boolean

    fun applyTheme()
}