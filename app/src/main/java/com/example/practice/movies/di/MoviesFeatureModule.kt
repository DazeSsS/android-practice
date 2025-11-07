package com.example.practice.movies.di

import com.example.practice.movies.data.api.MoviesApi
import com.example.practice.movies.data.mapper.MovieResponseToEntityMapper
import com.example.practice.movies.data.repository.MovieRepository
import com.example.practice.movies.domain.interactor.MovieInteractor
import com.example.practice.movies.presentation.model.BadgeCache
import com.example.practice.movies.presentation.viewModel.MovieDetailViewModel
import com.example.practice.movies.presentation.viewModel.MovieListViewModel
import com.example.practice.movies.presentation.viewModel.MovieSettingsViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit

val moviesFeatureModule = module {
    single { get<Retrofit>().create(MoviesApi::class.java) }

    factory { MovieResponseToEntityMapper() }
    single { MovieRepository(get(), get(), get(), get()) }

    single { MovieInteractor(get()) }

    single { BadgeCache() }

    viewModel { MovieDetailViewModel(get(), get(), get()) }
    viewModel { MovieListViewModel(get(), get(), get(), get()) }
    viewModel { MovieSettingsViewModel(get(), get(), get()) }
}