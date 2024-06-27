package com.akalugin.playlistmaker.data.sharing.impl

import android.content.Context
import android.content.res.Resources
import com.akalugin.playlistmaker.R
import com.akalugin.playlistmaker.domain.playlists.models.Playlist
import com.akalugin.playlistmaker.domain.sharing.ResourceRepository
import com.akalugin.playlistmaker.domain.sharing.model.EmailData

class ResourceRepositoryImpl(private val context: Context) : ResourceRepository {
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

    override fun playlistSharingData(playlist: Playlist): String =
        StringBuilder()
            .appendLine(playlist.name)
            .apply {
                if (playlist.description != null)
                    appendLine(playlist.description)
            }
            .appendLine(getTrackCount(context.resources, playlist.trackCount)).apply {
                playlist.tracks.forEachIndexed { index, track ->
                    appendLine(
                        context.getString(
                            R.string.track_to_share,
                            index + 1,
                            track.artistName,
                            track.trackName,
                            track.trackTime
                        )
                    )
                }
            }.toString()

    private fun getTrackCount(resources: Resources, trackCount: Int) =
        resources.getQuantityString(
            R.plurals.number_of_tracks,
            trackCount,
            trackCount
        )
}