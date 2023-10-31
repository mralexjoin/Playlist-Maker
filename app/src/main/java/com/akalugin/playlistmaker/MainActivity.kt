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
            R.id.buttonSearch to SearchActivity::class,
            R.id.buttonLibrary to LibraryActivity::class,
            R.id.buttonSettings to SettingsActivity::class,
        ).forEach { (buttonId, activityClass) ->
            findViewById<Button>(buttonId).setOnClickListener {
                startActivity(Intent(this@MainActivity, activityClass.java))
            }
        }
    }
}