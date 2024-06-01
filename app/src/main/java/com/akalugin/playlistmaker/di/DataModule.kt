package com.akalugin.playlistmaker.di

import android.content.Context
import androidx.room.Room
import com.akalugin.playlistmaker.BuildConfig
import com.akalugin.playlistmaker.data.db.AppDatabase
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
        Retrofit.Builder().baseUrl(BuildConfig.ITUNES_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ITunesApiService::class.java)
    }

    single {
        androidContext().getSharedPreferences(
            BuildConfig.PREFERENCES_NAME,
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

    single {
        Room.databaseBuilder(androidContext(), AppDatabase::class.java, "database.db")
            .build()
    }
}