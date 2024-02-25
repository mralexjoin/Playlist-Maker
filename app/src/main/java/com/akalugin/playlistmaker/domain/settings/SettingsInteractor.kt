package com.akalugin.playlistmaker.domain.settings

interface SettingsInteractor {
    val darkTheme: Boolean
    fun applyTheme()
    fun switchTheme()
}