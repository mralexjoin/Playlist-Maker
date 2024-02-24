package com.akalugin.playlistmaker.ui.utils

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.TypedValue
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

    inline fun <reified T : Serializable> Bundle.serializable(key: String): T? = when {
        Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU -> getSerializable(
            key,
            T::class.java
        )

        else -> @Suppress("Deprecation") getSerializable(key) as? T
    }
}
