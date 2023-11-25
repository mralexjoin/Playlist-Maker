package com.akalugin.playlistmaker

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar

class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        findViewById<Toolbar>(R.id.toolbar).setNavigationOnClickListener { finish() }
        findViewById<TextView>(R.id.shareTextView).setOnClickListener {
            val intent = Intent(Intent.ACTION_SEND).apply {
                type = "text/plain"
                putExtra(Intent.EXTRA_TEXT, getString(R.string.course_uri))
            }

            startActivity(Intent.createChooser(intent, null))
        }

        findViewById<TextView>(R.id.supportTextView).setOnClickListener {
            val intent = Intent(Intent.ACTION_SEND).apply {
                putExtra(Intent.EXTRA_EMAIL, arrayOf(getString(R.string.support_mail_address)))
                putExtra(Intent.EXTRA_SUBJECT, getString(R.string.support_mail_subject))
                putExtra(Intent.EXTRA_TEXT, getString(R.string.support_mail_text))
                selector = Intent(Intent.ACTION_SENDTO).apply {
                    data = Uri.parse("mailto:")
                }
            }

            startActivity(Intent.createChooser(intent, null))
        }

        findViewById<TextView>(R.id.userAgreementTextView).setOnClickListener {
            val intent =
                Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.user_agreement_uri)))

            startActivity(intent)
        }
    }
}