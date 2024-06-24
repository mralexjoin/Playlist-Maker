package com.akalugin.playlistmaker.ui.utils

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class ClickDebounce(
    private val coroutineScope: CoroutineScope,
    private val delayMillis: Long = DEFAULT_CLICK_DEBOUNCE_DELAY_MILLIS,
) {
    private var isClickAllowed = true
        get() = field.also {
            if (field) {
                field = false
                coroutineScope.launch {
                    delay(delayMillis)
                    field = true
                }
            }
        }

    fun debounce(action: () -> Unit) {
        if (isClickAllowed) action()
    }

    companion object {
        private const val DEFAULT_CLICK_DEBOUNCE_DELAY_MILLIS = 1_000L
    }
}