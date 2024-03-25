package com.akalugin.playlistmaker.ui.library.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.akalugin.playlistmaker.R
import com.akalugin.playlistmaker.databinding.ActivityLibraryBinding
import com.akalugin.playlistmaker.ui.library.adapter.LibraryPagerAdapter
import com.google.android.material.tabs.TabLayoutMediator

class LibraryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLibraryBinding
    private lateinit var tabLayoutMediator: TabLayoutMediator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLibraryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        with(binding) {
            toolbar.setNavigationOnClickListener { finish() }

            viewPager.adapter = LibraryPagerAdapter(supportFragmentManager, lifecycle)

            tabLayoutMediator = TabLayoutMediator(tabLayout, viewPager) { tab, position ->
                when (position) {
                    0 -> {
                        tab.text = getString(R.string.favorites_tracks)
                    }

                    1 -> {
                        tab.text = getString(R.string.playlists)
                    }
                }
            }
        }

        tabLayoutMediator.attach()
    }

    override fun onDestroy() {
        super.onDestroy()

        tabLayoutMediator.detach()
    }
}