package com.akalugin.playlistmaker

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.akalugin.playlistmaker.databinding.ActivitySettingsBinding

class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.apply {
            toolbar.setNavigationOnClickListener { finish() }

            shareTextView.setOnClickListener {
                val intent = Intent(Intent.ACTION_SEND).apply {
                    type = "text/plain"
                    putExtra(Intent.EXTRA_TEXT, getString(R.string.course_uri))
                }

                startActivity(Intent.createChooser(intent, null))
            }

            supportTextView.setOnClickListener {
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

            userAgreementTextView.setOnClickListener {
                val intent =
                    Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.user_agreement_uri)))

                startActivity(intent)
            }


            nightThemeSwitch.setOnCheckedChangeListener { _, checked ->
                (applicationContext as App).switchTheme(checked)
            }

            nightThemeSwitch.isChecked = (applicationContext as App).darkTheme
        }
    }
}