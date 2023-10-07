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
            R.id.search_button to SearchActivity::class,
            R.id.library_button to LibraryActivity::class,
            R.id.settings_button to SettingsActivity::class,
        ).forEach { (buttonId, activityClass) ->
            findViewById<Button>(buttonId).setOnClickListener {
                startActivity(Intent(this@MainActivity, activityClass.java))
            }
        }
    }
}