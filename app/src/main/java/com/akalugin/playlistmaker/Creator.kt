package com.akalugin.playlistmaker

import android.content.Context
import com.akalugin.playlistmaker.data.NetworkClient
import com.akalugin.playlistmaker.data.network.RetrofitNetworkClient
import com.akalugin.playlistmaker.data.repository.AudioPlayerImpl
import com.akalugin.playlistmaker.data.repository.SearchHistoryRepositoryImpl
import com.akalugin.playlistmaker.data.repository.TracksRepositoryImpl
import com.akalugin.playlistmaker.domain.api.AudioPlayer
import com.akalugin.playlistmaker.domain.api.SearchHistoryRepository
import com.akalugin.playlistmaker.domain.api.TracksInteractor
import com.akalugin.playlistmaker.domain.api.TracksRepository
import com.akalugin.playlistmaker.domain.impl.TracksInteractorImpl

object Creator {
    fun provideAudioPlayerInteractor(): AudioPlayer =
        AudioPlayerImpl()

    fun provideTracksInteractor(): TracksInteractor =
        TracksInteractorImpl(provideTracksRepository())
    fun provideSearchHistoryRepository(context: Context): SearchHistoryRepository =
        SearchHistoryRepositoryImpl(context)

    private fun provideTracksRepository(): TracksRepository =
        TracksRepositoryImpl(provideNetworkClient())

    private fun provideNetworkClient(): NetworkClient = RetrofitNetworkClient
}