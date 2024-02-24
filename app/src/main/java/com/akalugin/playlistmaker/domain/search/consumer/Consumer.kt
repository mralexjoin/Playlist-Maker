package com.akalugin.playlistmaker.domain.search.consumer

interface Consumer<T> {
    fun consume(data: ConsumerData<T>)
}
