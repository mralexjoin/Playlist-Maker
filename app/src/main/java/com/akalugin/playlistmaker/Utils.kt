package com.akalugin.playlistmaker

import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.TypedValue
import java.io.Serializable
import java.text.SimpleDateFormat
import java.util.Locale

fun dpToPx(dp: Float, context: Context) = TypedValue.applyDimension(
    TypedValue.COMPLEX_UNIT_DIP, dp, context.resources.displayMetrics
).toInt()

inline fun <reified T : Serializable> Intent.serializable(key: String): T? = when {
    Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU -> getSerializableExtra(
        key,
        T::class.java
    )

    else -> @Suppress("DEPRECATION") getSerializableExtra(key) as? T
}

fun formatMilliseconds(milliseconds: Int): String =
    SimpleDateFormat("mm:ss", Locale.getDefault()).format(milliseconds)