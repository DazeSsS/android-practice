package com.example.practice

import android.app.Application
import com.example.practice.di.dbModule
import com.example.practice.di.mainModule
import com.example.practice.movies.di.moviesFeatureModule
import com.example.practice.di.networkModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger()
            androidContext(this@App)
            modules(mainModule, moviesFeatureModule, networkModule, dbModule)
        }
    }
}