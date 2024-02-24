package com.akalugin.playlistmaker.creator

import android.content.Context
import android.media.MediaPlayer
import com.akalugin.playlistmaker.data.search.network.NetworkClient
import com.akalugin.playlistmaker.data.search.network.impl.RetrofitNetworkClient
import com.akalugin.playlistmaker.data.player.impl.AudioPlayerRepositoryImpl
import com.akalugin.playlistmaker.data.search.impl.SearchHistoryRepositoryImpl
import com.akalugin.playlistmaker.data.search.impl.TracksRepositoryImpl
import com.akalugin.playlistmaker.data.settings.impl.SettingsRepositoryImpl
import com.akalugin.playlistmaker.data.sharing.impl.ExternalNavigatorImpl
import com.akalugin.playlistmaker.data.sharing.impl.ResourceRepositoryImpl
import com.akalugin.playlistmaker.domain.player.AudioPlayerInteractor
import com.akalugin.playlistmaker.domain.player.AudioPlayerRepository
import com.akalugin.playlistmaker.domain.search.history.SearchHistoryInteractor
import com.akalugin.playlistmaker.domain.search.history.SearchHistoryRepository
import com.akalugin.playlistmaker.domain.search.tracks.TracksInteractor
import com.akalugin.playlistmaker.domain.search.tracks.TracksRepository
import com.akalugin.playlistmaker.domain.player.impl.AudioPlayerInteractorImpl
import com.akalugin.playlistmaker.domain.search.impl.SearchHistoryInteractorImpl
import com.akalugin.playlistmaker.domain.search.impl.TracksInteractorImpl
import com.akalugin.playlistmaker.domain.settings.SettingsInteractor
import com.akalugin.playlistmaker.domain.settings.SettingsRepository
import com.akalugin.playlistmaker.domain.settings.impl.SettingsInteractorImpl
import com.akalugin.playlistmaker.domain.sharing.ExternalNavigator
import com.akalugin.playlistmaker.domain.sharing.ResourceRepository
import com.akalugin.playlistmaker.domain.sharing.SharingInteractor
import com.akalugin.playlistmaker.domain.sharing.impl.SharingInteractorImpl

object Creator {

    fun provideAudioPlayerInteractor(): AudioPlayerInteractor =
        AudioPlayerInteractorImpl(provideAudioPlayerRepository())

    fun provideSearchHistoryInteractor(context: Context): SearchHistoryInteractor =
        SearchHistoryInteractorImpl(provideSearchHistoryRepository(context))

    fun provideSettingsInteractor(context: Context): SettingsInteractor =
        SettingsInteractorImpl(provideSettingsRepository(context))

    fun provideSharingInteractor(context: Context): SharingInteractor =
        SharingInteractorImpl(
            provideExternalNavigator(context),
            provideResourceRepository(context),
        )

    fun provideTracksInteractor(): TracksInteractor =
        TracksInteractorImpl(provideTracksRepository())

    private fun provideAudioPlayerRepository(): AudioPlayerRepository =
        AudioPlayerRepositoryImpl(MediaPlayer())

    private fun provideExternalNavigator(context: Context): ExternalNavigator =
        ExternalNavigatorImpl(context)

    private fun provideNetworkClient(): NetworkClient = RetrofitNetworkClient

    private fun provideResourceRepository(context: Context): ResourceRepository =
        ResourceRepositoryImpl(context)

    private fun provideSearchHistoryRepository(context: Context): SearchHistoryRepository =
        SearchHistoryRepositoryImpl(context)

    private fun provideSettingsRepository(context: Context): SettingsRepository =
        SettingsRepositoryImpl(context)

    private fun provideTracksRepository(): TracksRepository =
        TracksRepositoryImpl(provideNetworkClient())

}