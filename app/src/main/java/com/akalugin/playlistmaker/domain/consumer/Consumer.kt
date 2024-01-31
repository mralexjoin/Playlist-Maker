package com.akalugin.playlistmaker.domain.consumer

interface Consumer<T> {
    fun consume(data: ConsumerData<T>)
}
