package com.akalugin.playlistmaker.ui.utils

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.util.TypedValue
import androidx.annotation.IdRes
import androidx.core.os.bundleOf
import androidx.navigation.NavController
import com.akalugin.playlistmaker.domain.track.models.Track
import com.akalugin.playlistmaker.ui.player.fragments.AudioPlayerFragment
import java.io.Serializable

object Utils {
    fun dpToPx(dp: Float, context: Context) = TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP, dp, context.resources.displayMetrics
    ).toInt()

    inline fun <reified T : Serializable> Bundle.serializable(key: String): T? = when {
        Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU -> getSerializable(
            key,
            T::class.java
        )

        else -> @Suppress("Deprecation") getSerializable(key) as? T
    }

    fun playTrack(navController: NavController, @IdRes actionId: Int, track: Track) {
        navController.navigate(actionId, bundleOf(AudioPlayerFragment.TRACK_KEY_EXTRA to track))
    }
}
