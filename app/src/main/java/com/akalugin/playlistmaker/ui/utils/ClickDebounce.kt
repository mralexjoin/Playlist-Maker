package com.akalugin.playlistmaker.ui.utils

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class ClickDebounce(
    private val coroutineScope: CoroutineScope,
    private val delayMillis: Long,
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
}