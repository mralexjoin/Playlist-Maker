package com.akalugin.playlistmaker.domain.formatter

import java.text.SimpleDateFormat
import java.util.Locale

object Formatter {
    fun formatMilliseconds(milliseconds: Int): String =
        SimpleDateFormat("mm:ss", Locale.getDefault()).format(milliseconds)
}