package com.akalugin.playlistmaker.ui.library.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.akalugin.playlistmaker.ui.library.favorites.FavoritesFragment
import com.akalugin.playlistmaker.ui.library.playlists.PlaylistsFragment

class LibraryPagerAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle) :
    FragmentStateAdapter(fragmentManager, lifecycle) {
    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment =
        when (position) {
            0 -> FavoritesFragment.newInstance()
            else -> PlaylistsFragment.newInstance()
        }
}