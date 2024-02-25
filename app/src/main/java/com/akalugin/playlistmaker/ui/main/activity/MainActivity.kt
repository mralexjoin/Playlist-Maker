package com.akalugin.playlistmaker.ui.main.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.akalugin.playlistmaker.databinding.ActivityMainBinding
import com.akalugin.playlistmaker.ui.library.activity.LibraryActivity
import com.akalugin.playlistmaker.ui.search.activity.SearchActivity
import com.akalugin.playlistmaker.ui.settings.activity.SettingsActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        with(binding) {
            mapOf(
                searchButton to SearchActivity::class,
                libraryButton to LibraryActivity::class,
                settingsButton to SettingsActivity::class,
            ).forEach { (button, activityClass) ->
                button.setOnClickListener {
                    startActivity(Intent(this@MainActivity, activityClass.java))
                }
            }
        }
    }
}