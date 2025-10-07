package com.example.practice.di

import com.example.practice.Movies
import com.example.practice.movies.presentation.viewModel.MovieDetailViewModel
import com.example.practice.movies.presentation.viewModel.MovieListViewModel
import com.example.practice.navigation.Route
import com.example.practice.navigation.TopLevelBackStack
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val mainModule = module {
    single { TopLevelBackStack<Route>(Movies) }

    viewModel { MovieDetailViewModel(get(), get()) }
    viewModel { MovieListViewModel(get(), get()) }
}