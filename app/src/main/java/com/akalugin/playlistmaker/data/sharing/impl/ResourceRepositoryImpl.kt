package com.akalugin.playlistmaker.data.sharing.impl

import android.content.Context
import com.akalugin.playlistmaker.R
import com.akalugin.playlistmaker.domain.sharing.ResourceRepository
import com.akalugin.playlistmaker.domain.sharing.model.EmailData

class ResourceRepositoryImpl(private val context: Context): ResourceRepository {
    override val shareAppLink: String
        get() = context.getString(R.string.course_uri)

    override val supportEmailData: EmailData
        get() = EmailData(
            address = context.getString(R.string.support_mail_address),
            subject = context.getString(R.string.support_mail_subject),
            text = context.getString(R.string.support_mail_text),
        )

    override val termsLink: String
        get() = context.getString(R.string.user_agreement_uri)
}