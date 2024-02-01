package com.akalugin.playlistmaker

import android.content.Context
import android.media.MediaPlayer
import com.akalugin.playlistmaker.data.NetworkClient
import com.akalugin.playlistmaker.data.network.RetrofitNetworkClient
import com.akalugin.playlistmaker.data.repository.AudioPlayerRepositoryImpl
import com.akalugin.playlistmaker.data.repository.SearchHistoryRepositoryImpl
import com.akalugin.playlistmaker.data.repository.TracksRepositoryImpl
import com.akalugin.playlistmaker.domain.api.audio_player.AudioPlayerInteractor
import com.akalugin.playlistmaker.domain.api.audio_player.AudioPlayerRepository
import com.akalugin.playlistmaker.domain.api.search_history.SearchHistoryInteractor
import com.akalugin.playlistmaker.domain.api.search_history.SearchHistoryRepository
import com.akalugin.playlistmaker.domain.api.tracks.TracksInteractor
import com.akalugin.playlistmaker.domain.api.tracks.TracksRepository
import com.akalugin.playlistmaker.domain.impl.AudioPlayerInteractorImpl
import com.akalugin.playlistmaker.domain.impl.SearchHistoryInteractorImpl
import com.akalugin.playlistmaker.domain.impl.TracksInteractorImpl

object Creator {

    fun provideAudioPlayerInteractor(): AudioPlayerInteractor =
        AudioPlayerInteractorImpl(provideAudioPlayerRepository())
    fun provideTracksInteractor(): TracksInteractor =
        TracksInteractorImpl(provideTracksRepository())
    fun provideSearchHistoryInteractor(context: Context): SearchHistoryInteractor =
        SearchHistoryInteractorImpl(provideSearchHistoryRepository(context))

    private fun provideAudioPlayerRepository(): AudioPlayerRepository =
        AudioPlayerRepositoryImpl(MediaPlayer())
    private fun provideTracksRepository(): TracksRepository =
        TracksRepositoryImpl(provideNetworkClient())
    private fun provideSearchHistoryRepository(context: Context): SearchHistoryRepository =
        SearchHistoryRepositoryImpl(context)

    private fun provideNetworkClient(): NetworkClient = RetrofitNetworkClient
}