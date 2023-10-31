package com.akalugin.playlistmaker

import android.content.Context
import android.util.TypedValue

fun dpToPx(dp: Float, context: Context) = TypedValue.applyDimension(
    TypedValue.COMPLEX_UNIT_DIP, dp, context.resources.displayMetrics
).toInt()
