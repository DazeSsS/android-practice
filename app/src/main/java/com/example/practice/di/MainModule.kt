package com.example.practice.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStoreFile
import com.example.practice.Movies
import com.example.practice.navigation.Route
import com.example.practice.navigation.TopLevelBackStack
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val mainModule = module {
    single { TopLevelBackStack<Route>(Movies) }

    single {
        getDataStore(androidContext())
    }
}

fun getDataStore(androidContext: Context): DataStore<Preferences> =
    PreferenceDataStoreFactory.create {
        androidContext.preferencesDataStoreFile("default")
    }