package com.example.practice.di

import android.content.Context
import androidx.room.Room
import com.example.practice.movies.data.db.MovieDatabase
import org.koin.dsl.module

val dbModule = module {
    single { DatabaseBuilder.getInstance(get()) }
}

object DatabaseBuilder {

    fun getInstance(context: Context) =
        buildRoomDB(context)

    private fun buildRoomDB(context: Context) =
        Room.databaseBuilder(
            context.applicationContext,
            MovieDatabase::class.java,
            "movies"
        ).build()
}