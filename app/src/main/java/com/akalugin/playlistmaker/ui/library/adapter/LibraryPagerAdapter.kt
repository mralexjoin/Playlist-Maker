package com.akalugin.playlistmaker.ui.library.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.akalugin.playlistmaker.ui.library.favorites.fragments.FavoritesFragment
import com.akalugin.playlistmaker.ui.library.playlists.list.fragments.PlaylistsFragment

class LibraryPagerAdapter(fragment: Fragment) :
    FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment =
        when (position) {
            0 -> FavoritesFragment.newInstance()
            else -> PlaylistsFragment.newInstance()
        }
}