package com.example.practice.di

import com.example.practice.movies.data.MoviesApi
import com.example.practice.movies.data.mapper.MovieResponseToEntityMapper
import com.example.practice.movies.data.repository.MovieRepository
import com.example.practice.movies.domain.interactor.MovieInteractor
import com.example.practice.movies.presentation.viewModel.MovieDetailViewModel
import com.example.practice.movies.presentation.viewModel.MovieListViewModel
import com.example.practice.movies.presentation.viewModel.MovieSettingsViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit

val moviesFeatureModule = module {
    single { get<Retrofit>().create(MoviesApi::class.java) }

    factory { MovieResponseToEntityMapper() }
    single { MovieRepository(get(), get(), get()) }

    single { MovieInteractor(get()) }

    viewModel { MovieDetailViewModel(get(), get()) }
    viewModel { MovieListViewModel(get(), get()) }
    viewModel { MovieSettingsViewModel(get(), get()) }
}