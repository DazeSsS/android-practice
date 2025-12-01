package com.example.movies.di

import androidx.compose.ui.window.DialogProperties
import androidx.navigation3.scene.DialogSceneStrategy
import com.example.core.navigation.EntryProviderInstaller
import com.example.core.navigation.Route
import com.example.movies.data.api.MoviesApi
import com.example.movies.data.mapper.MovieResponseToEntityMapper
import com.example.movies.data.repository.MovieRepository
import com.example.movies.domain.interactor.MovieInteractor
import com.example.movies.presentation.model.BadgeCache
import com.example.movies.presentation.model.MovieUiModel
import com.example.movies.presentation.screen.MovieDetailScreen
import com.example.movies.presentation.screen.MovieSettingsDialog
import com.example.movies.presentation.viewModel.MovieDetailViewModel
import com.example.movies.presentation.viewModel.MovieListViewModel
import com.example.movies.presentation.viewModel.MovieSettingsViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit

const val MOVIE_QUALIFIER = "movieQualifier"

data class MovieDetail(val movie: MovieUiModel) : Route

data object MovieSettings : Route

val moviesFeatureModule = module {
    single { get<Retrofit>().create(MoviesApi::class.java) }

    factory { MovieResponseToEntityMapper() }
    single { MovieRepository(get(), get(), get(), get()) }

    single { MovieInteractor(get()) }

    single { BadgeCache() }

    viewModel { MovieDetailViewModel(get(), get(), get()) }
    viewModel { MovieListViewModel(get(), get(), get(), get()) }
    viewModel { MovieSettingsViewModel(get(), get(), get()) }

    factory<EntryProviderInstaller>(qualifier = named(MOVIE_QUALIFIER)) {
        {
            entry<MovieDetail> {
                MovieDetailScreen(it.movie)
            }
            entry<MovieSettings>(
                metadata = DialogSceneStrategy.dialog(DialogProperties())
            ) {
                MovieSettingsDialog()
            }
        }
    }
}