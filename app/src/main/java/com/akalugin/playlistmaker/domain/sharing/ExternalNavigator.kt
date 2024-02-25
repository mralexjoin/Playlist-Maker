package com.akalugin.playlistmaker.domain.sharing

import com.akalugin.playlistmaker.domain.sharing.model.EmailData

interface ExternalNavigator {
    fun shareLink(link: String)
    fun openLink(link: String)
    fun openEmail(emailData: EmailData)
}