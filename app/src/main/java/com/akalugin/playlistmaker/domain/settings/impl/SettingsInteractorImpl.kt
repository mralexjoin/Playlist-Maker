package com.akalugin.playlistmaker.domain.settings.impl

import com.akalugin.playlistmaker.domain.settings.SettingsInteractor
import com.akalugin.playlistmaker.domain.settings.SettingsRepository

class SettingsInteractorImpl(
    private val settingsRepository: SettingsRepository,
) : SettingsInteractor {
    override val darkTheme: Boolean
        get() = settingsRepository.darkTheme

    override fun applyTheme() {
        settingsRepository.applyTheme()
    }

    override fun switchTheme() {
        settingsRepository.darkTheme = !settingsRepository.darkTheme
    }
}