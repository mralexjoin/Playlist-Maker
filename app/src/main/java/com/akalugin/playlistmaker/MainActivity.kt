package com.akalugin.playlistmaker

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mapOf(
            R.id.searchButton to SearchActivity::class,
            R.id.libraryButton to LibraryActivity::class,
            R.id.settingsButton to SettingsActivity::class,
        ).forEach { (buttonId, activityClass) ->
            findViewById<Button>(buttonId).setOnClickListener {
                startActivity(Intent(this@MainActivity, activityClass.java))
            }
        }
    }
}