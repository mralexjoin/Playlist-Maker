package com.akalugin.playlistmaker.di

import android.media.MediaPlayer
import com.akalugin.playlistmaker.App
import com.akalugin.playlistmaker.data.player.impl.AudioPlayerRepositoryImpl
import com.akalugin.playlistmaker.data.search.impl.SearchHistoryRepositoryImpl
import com.akalugin.playlistmaker.data.search.impl.TracksRepositoryImpl
import com.akalugin.playlistmaker.data.settings.impl.SettingsRepositoryImpl
import com.akalugin.playlistmaker.data.sharing.impl.ResourceRepositoryImpl
import com.akalugin.playlistmaker.domain.player.AudioPlayerRepository
import com.akalugin.playlistmaker.domain.search.history.SearchHistoryRepository
import com.akalugin.playlistmaker.domain.search.tracks.TracksRepository
import com.akalugin.playlistmaker.domain.settings.SettingsRepository
import com.akalugin.playlistmaker.domain.sharing.ResourceRepository
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val repositoryModule = module {
    factory<AudioPlayerRepository> {
        AudioPlayerRepositoryImpl(get())
    }

    factory {
        MediaPlayer()
    }

    single<ResourceRepository> {
        ResourceRepositoryImpl(androidContext())
    }

    single<SearchHistoryRepository> {
        SearchHistoryRepositoryImpl(get(), get())
    }

    single<SettingsRepository> {
        SettingsRepositoryImpl(androidContext() as App, get())
    }

    single<TracksRepository> {
        TracksRepositoryImpl(get())
    }
}