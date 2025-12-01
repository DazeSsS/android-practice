package com.example.practice.di

import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.example.movies.BASE_API_URL
import com.example.movies.MOVIE_API_KEY
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory

val networkModule = module {
    single {
        OkHttpClient.Builder()
            .addInterceptor { chain ->
                val request = chain.request().newBuilder()
                    .header("X-API-KEY", MOVIE_API_KEY)
                    .build()
                chain.proceed(request)
            }
            .addInterceptor(ChuckerInterceptor(get()))
            .build()
    }

    single {
        val json = Json {
            explicitNulls = false
            ignoreUnknownKeys = true
        }
        Retrofit.Builder()
            .baseUrl(BASE_API_URL)
            .addConverterFactory(
                json.asConverterFactory(
                    "application/json; charset=UTF-8".toMediaType()
                )
            )
            .client(get())
            .build()
    }
}