package com.akalugin.playlistmaker

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<Button>(R.id.settings_button).setOnClickListener(
            object : View.OnClickListener{
                override fun onClick(v: View?) = showToastForButton(v!!)
            }
        )
    }

    private fun showToastForButton(view: View) =
        Toast.makeText(
            this,
            "Нажата кнопка ${(view as Button).text}",
            Toast.LENGTH_SHORT
        ).show()
}