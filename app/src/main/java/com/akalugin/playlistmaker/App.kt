package com.akalugin.playlistmaker

import android.app.Application
import com.akalugin.playlistmaker.creator.Creator

class App : Application() {
    override fun onCreate() {
        super.onCreate()

        Creator.provideSettingsInteractor(applicationContext).applyTheme()
    }
}