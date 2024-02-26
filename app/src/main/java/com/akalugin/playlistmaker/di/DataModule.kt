package com.akalugin.playlistmaker.di

import android.content.Context
import com.akalugin.playlistmaker.data.search.network.NetworkClient
import com.akalugin.playlistmaker.data.search.network.impl.ITunesApiService
import com.akalugin.playlistmaker.data.search.network.impl.RetrofitNetworkClient
import com.akalugin.playlistmaker.data.sharing.impl.ExternalNavigatorImpl
import com.akalugin.playlistmaker.domain.sharing.ExternalNavigator
import com.google.gson.Gson
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val dataModule = module {
    single<ITunesApiService> {
        Retrofit.Builder().baseUrl(getProperty<String>("itunes_url"))
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ITunesApiService::class.java)
    }

    single {
        androidContext().getSharedPreferences(
            getProperty("preferences_name"),
            Context.MODE_PRIVATE
        )
    }

    factoryOf(::Gson)

    single<NetworkClient> {
        RetrofitNetworkClient(get())
    }

    single<ExternalNavigator> {
        ExternalNavigatorImpl(androidContext())
    }
}