package com.akalugin.playlistmaker.ui.utils

import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.TypedValue
import androidx.fragment.app.Fragment
import com.akalugin.playlistmaker.domain.track.models.Track
import com.akalugin.playlistmaker.ui.player.activity.AudioPlayerActivity
import java.io.Serializable

object Utils {
    fun dpToPx(dp: Float, context: Context) = TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP, dp, context.resources.displayMetrics
    ).toInt()

    inline fun <reified T : Serializable> Intent.serializable(key: String): T? = when {
        Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU -> getSerializableExtra(
            key,
            T::class.java
        )

        else -> @Suppress("Deprecation") getSerializableExtra(key) as? T
    }

    fun playTrack(fragment: Fragment, track: Track) {
        val intent = Intent(fragment.requireContext(), AudioPlayerActivity::class.java).apply {
            putExtra(AudioPlayerActivity.TRACK_KEY_EXTRA, track)
        }
        fragment.startActivity(intent)
    }
}
