package com.akalugin.playlistmaker

import android.app.Application
import com.akalugin.playlistmaker.di.dataModule
import com.akalugin.playlistmaker.di.interactorModule
import com.akalugin.playlistmaker.di.repositoryModule
import com.akalugin.playlistmaker.di.viewModelModule
import com.akalugin.playlistmaker.domain.settings.SettingsInteractor
import org.koin.android.ext.android.get
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@App)
            modules(dataModule, repositoryModule, interactorModule, viewModelModule)
        }

        get<SettingsInteractor>().applyTheme()
    }
}