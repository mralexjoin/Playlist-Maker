package com.akalugin.playlistmaker.domain.sharing

import com.akalugin.playlistmaker.domain.playlists.models.Playlist

interface SharingInteractor {
    fun shareApp()
    fun openTerms()
    fun openSupport()
    fun sharePlaylist(playlist: Playlist)
}