package com.akalugin.playlistmaker.ui.settings.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.akalugin.playlistmaker.domain.settings.SettingsInteractor
import com.akalugin.playlistmaker.domain.sharing.SharingInteractor

class SettingsViewModel(
    private val sharingInteractor: SharingInteractor,
    private val settingsInteractor: SettingsInteractor,
) : ViewModel() {

    private val mNightThemeActiveLiveData = MutableLiveData(settingsInteractor.darkTheme)
    val nightThemeActiveLiveData: LiveData<Boolean>
        get() = mNightThemeActiveLiveData

    fun shareApp() = sharingInteractor.shareApp()
    fun openTerms() = sharingInteractor.openTerms()
    fun openSupport() = sharingInteractor.openSupport()

    fun switchTheme() {
        settingsInteractor.switchTheme()

        mNightThemeActiveLiveData.value = settingsInteractor.darkTheme
    }
}