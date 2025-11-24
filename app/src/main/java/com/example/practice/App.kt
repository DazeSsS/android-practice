package com.example.practice

import android.app.Application
import androidx.core.app.NotificationManagerCompat
import com.example.practice.di.dbModule
import com.example.practice.di.mainModule
import com.example.practice.movies.di.moviesFeatureModule
import com.example.practice.di.networkModule
import com.example.practice.profile.di.profileFeatureModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class App : Application() {

    private val channelManager : NotificationChannelManager by lazy {
        NotificationChannelManager(NotificationManagerCompat.from(this), this)
    }

    override fun onCreate() {
        super.onCreate()
        channelManager.createNotificationChannels()
        startKoin {
            androidLogger()
            androidContext(this@App)
            modules(mainModule, moviesFeatureModule, profileFeatureModule, networkModule, dbModule)
        }
    }
}