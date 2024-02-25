package com.akalugin.playlistmaker.ui.settings.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.akalugin.playlistmaker.creator.Creator
import com.akalugin.playlistmaker.databinding.ActivitySettingsBinding
import com.akalugin.playlistmaker.ui.settings.view_model.SettingsViewModel

class SettingsActivity : AppCompatActivity() {

    private lateinit var viewModel: SettingsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(
            this, SettingsViewModel.getViewModelFactory(
                Creator.provideSharingInteractor(applicationContext),
                Creator.provideSettingsInteractor(applicationContext),
            )
        )[SettingsViewModel::class.java]

        with(binding) {
            toolbar.setNavigationOnClickListener { finish() }

            shareTextView.setOnClickListener {
                viewModel.shareApp()
            }

            supportTextView.setOnClickListener {
                viewModel.openSupport()
            }

            userAgreementTextView.setOnClickListener {
                viewModel.openTerms()
            }

            viewModel.nightThemeActiveLiveData.observe(this@SettingsActivity) { isChecked ->
                nightThemeSwitch.isChecked = isChecked
            }

            nightThemeSwitch.setOnClickListener {
                viewModel.switchTheme()
            }
        }
    }
}