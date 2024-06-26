package com.akalugin.playlistmaker.di

import com.akalugin.playlistmaker.domain.playlists.models.Playlist
import com.akalugin.playlistmaker.domain.track.models.Track
import com.akalugin.playlistmaker.ui.library.favorites.view_model.FavoritesViewModel
import com.akalugin.playlistmaker.ui.library.playlists.creation.view_model.EditPlaylistViewModel
import com.akalugin.playlistmaker.ui.library.playlists.list.view_model.PlaylistsViewModel
import com.akalugin.playlistmaker.ui.library.playlists.playlist.view_model.PlaylistViewModel
import com.akalugin.playlistmaker.ui.player.view_model.AudioPlayerViewModel
import com.akalugin.playlistmaker.ui.search.view_model.SearchViewModel
import com.akalugin.playlistmaker.ui.settings.view_model.SettingsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { (track: Track) ->
        AudioPlayerViewModel(track, get(), get(), get())
    }

    viewModelOf(::SearchViewModel)

    viewModelOf(::SettingsViewModel)

    viewModelOf(::FavoritesViewModel)
    viewModelOf(::PlaylistsViewModel)
    viewModelOf(::PlaylistViewModel)

    viewModel { (playlist: Playlist) ->
        EditPlaylistViewModel(playlist, get(), get())
    }
}