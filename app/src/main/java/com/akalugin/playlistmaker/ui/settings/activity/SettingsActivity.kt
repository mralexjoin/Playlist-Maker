package com.akalugin.playlistmaker.ui.settings.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.akalugin.playlistmaker.databinding.ActivitySettingsBinding
import com.akalugin.playlistmaker.ui.settings.view_model.SettingsViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class SettingsActivity : AppCompatActivity() {

    private val viewModel: SettingsViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        with(binding) {
            toolbar.setNavigationOnClickListener { finish() }

            with (viewModel) {
                shareTextView.setOnClickListener {
                    shareApp()
                }

                supportTextView.setOnClickListener {
                    openSupport()
                }

                userAgreementTextView.setOnClickListener {
                    openTerms()
                }

                nightThemeActiveLiveData.observe(this@SettingsActivity) { isChecked ->
                    nightThemeSwitch.isChecked = isChecked
                }

                nightThemeSwitch.setOnClickListener {
                    switchTheme()
                }
            }
        }
    }
}