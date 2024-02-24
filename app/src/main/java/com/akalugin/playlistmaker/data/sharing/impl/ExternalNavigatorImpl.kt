package com.akalugin.playlistmaker.data.sharing.impl

import android.content.Context
import android.content.Intent
import android.net.Uri
import com.akalugin.playlistmaker.domain.sharing.ExternalNavigator
import com.akalugin.playlistmaker.domain.sharing.model.EmailData

class ExternalNavigatorImpl(private val context: Context) : ExternalNavigator {
    override fun shareLink(link: String) = with(Intent(Intent.ACTION_SEND)) {
        type = "text/plain"
        putExtra(Intent.EXTRA_TEXT, link)

        startActivityNewTask(chooserIntent(this))
    }

    override fun openEmail(emailData: EmailData) = with(Intent(Intent.ACTION_SEND)) {
        putExtra(Intent.EXTRA_EMAIL, arrayOf(emailData.address))
        putExtra(Intent.EXTRA_SUBJECT, emailData.subject)
        putExtra(Intent.EXTRA_TEXT, emailData.text)
        selector = Intent(Intent.ACTION_SENDTO).apply {
            data = Uri.parse("mailto:")
        }

        startActivityNewTask(chooserIntent(this))
    }

    override fun openLink(link: String) =
        startActivityNewTask(Intent(Intent.ACTION_VIEW, Uri.parse(link)))

    private fun chooserIntent(intent: Intent) = Intent.createChooser(intent, null)

    private fun startActivityNewTask(intent: Intent) =
        context.startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
}