package com.akalugin.playlistmaker.domain.sharing.impl

import com.akalugin.playlistmaker.domain.playlists.models.Playlist
import com.akalugin.playlistmaker.domain.sharing.ExternalNavigator
import com.akalugin.playlistmaker.domain.sharing.ResourceRepository
import com.akalugin.playlistmaker.domain.sharing.SharingInteractor
import com.akalugin.playlistmaker.domain.sharing.model.EmailData

class SharingInteractorImpl(
    private val externalNavigator: ExternalNavigator,
    private val resourceRepository: ResourceRepository,
) : SharingInteractor {
    override fun shareApp() {
        externalNavigator.shareLink(getShareAppLink())
    }

    override fun sharePlaylist(playlist: Playlist) {
        externalNavigator.shareLink(resourceRepository.playlistSharingData(playlist))
    }

    override fun openTerms() {
        externalNavigator.openLink(getTermsLink())
    }

    override fun openSupport() {
        externalNavigator.openEmail(getSupportEmailData())
    }

    private fun getShareAppLink(): String = resourceRepository.shareAppLink

    private fun getSupportEmailData(): EmailData = resourceRepository.supportEmailData

    private fun getTermsLink(): String = resourceRepository.termsLink
}