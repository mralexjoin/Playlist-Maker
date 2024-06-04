package com.akalugin.playlistmaker.domain.sharing

import com.akalugin.playlistmaker.domain.playlists.models.Playlist
import com.akalugin.playlistmaker.domain.sharing.model.EmailData

interface ResourceRepository {
    val shareAppLink: String

    val supportEmailData: EmailData

    val termsLink: String

    fun playlistSharingData(playlist: Playlist): String
}