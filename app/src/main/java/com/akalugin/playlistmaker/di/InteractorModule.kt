package com.akalugin.playlistmaker.di

import com.akalugin.playlistmaker.domain.favorites.FavoriteTracksInteractor
import com.akalugin.playlistmaker.domain.favorites.impl.FavoriteTracksInteractorImpl
import com.akalugin.playlistmaker.domain.files.FileInteractor
import com.akalugin.playlistmaker.domain.files.PermissionInteractor
import com.akalugin.playlistmaker.domain.files.impl.FileInteractorImpl
import com.akalugin.playlistmaker.domain.files.impl.PermissionInteractorImpl
import com.akalugin.playlistmaker.domain.player.AudioPlayerInteractor
import com.akalugin.playlistmaker.domain.player.impl.AudioPlayerInteractorImpl
import com.akalugin.playlistmaker.domain.playlists.PlaylistInteractor
import com.akalugin.playlistmaker.domain.playlists.impl.PlaylistInteractorImpl
import com.akalugin.playlistmaker.domain.search.history.SearchHistoryInteractor
import com.akalugin.playlistmaker.domain.search.impl.SearchHistoryInteractorImpl
import com.akalugin.playlistmaker.domain.search.impl.TracksInteractorImpl
import com.akalugin.playlistmaker.domain.search.tracks.TracksInteractor
import com.akalugin.playlistmaker.domain.settings.SettingsInteractor
import com.akalugin.playlistmaker.domain.settings.impl.SettingsInteractorImpl
import com.akalugin.playlistmaker.domain.sharing.SharingInteractor
import com.akalugin.playlistmaker.domain.sharing.impl.SharingInteractorImpl
import org.koin.dsl.module

val interactorModule = module {
    factory<AudioPlayerInteractor> {
        AudioPlayerInteractorImpl(get())
    }

    single<SearchHistoryInteractor> {
        SearchHistoryInteractorImpl(get())
    }

    single<SettingsInteractor> {
        SettingsInteractorImpl(get())
    }

    single<SharingInteractor> {
        SharingInteractorImpl(get(), get())
    }

    single<TracksInteractor> {
        TracksInteractorImpl(get())
    }

    single<FavoriteTracksInteractor> {
        FavoriteTracksInteractorImpl(get())
    }

    single<PlaylistInteractor> {
        PlaylistInteractorImpl(get())
    }

    single<PermissionInteractor> {
        PermissionInteractorImpl(get())
    }

    single<FileInteractor> {
        FileInteractorImpl(get())
    }
}