package com.akalugin.playlistmaker.di

import com.akalugin.playlistmaker.ui.player.view_model.AudioPlayerViewModel
import com.akalugin.playlistmaker.ui.search.view_model.SearchViewModel
import com.akalugin.playlistmaker.ui.settings.view_model.SettingsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { (previewUrl: String) ->
        AudioPlayerViewModel(previewUrl, get())
    }

    viewModelOf(::SearchViewModel)

    viewModelOf(::SettingsViewModel)
}