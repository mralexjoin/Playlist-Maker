package com.akalugin.playlistmaker.ui.root

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.akalugin.playlistmaker.R
import com.akalugin.playlistmaker.databinding.ActivityRootBinding

class RootActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRootBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRootBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.rootFragmentContainerView) as NavHostFragment
        val navController = navHostFragment.navController
        val appBarConfiguration = AppBarConfiguration(navController.graph)

        with(binding) {
            bottomNavigationView.setupWithNavController(navController)
            toolbar.setupWithNavController(navController, appBarConfiguration)
            toolbar.setNavigationOnClickListener {
                onBackPressedDispatcher.onBackPressed()
            }

            navController.addOnDestinationChangedListener { _, destination, _ ->
                bottomNavigationView.isVisible = when (destination.id) {
                    R.id.libraryFragment, R.id.searchFragment, R.id.settingsFragment -> true
                    else -> false
                }
            }
        }
    }

    fun hideToolbar() {
        binding.toolbar.isVisible = false
    }

    fun showToolbar() {
        binding.toolbar.isVisible = true
    }
}