package com.example.practice.movies.presentation.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.practice.MovieDetail
import com.example.practice.MovieSettings
import com.example.practice.core.launchLoadingAndError
import com.example.practice.movies.domain.interactor.MovieInteractor
import com.example.practice.movies.domain.model.MovieEntity
import com.example.practice.movies.presentation.model.MovieListViewState
import com.example.practice.movies.presentation.model.MovieUiModel
import com.example.practice.navigation.Route
import com.example.practice.navigation.TopLevelBackStack
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update

class MovieListViewModel(
    private val topLevelBackStack: TopLevelBackStack<Route>,
    private val interactor: MovieInteractor,
) : ViewModel() {
    private val mutableState = MutableStateFlow(MovieListViewState())
    val state = mutableState.asStateFlow()

    init {
        loadMovies()
    }

    fun onMovieClick(movie: MovieUiModel) {
        topLevelBackStack.add(MovieDetail(movie))
    }

    fun onRetryClick() = loadMovies()

    fun onSettingsClick() = topLevelBackStack.add(MovieSettings)

    private fun loadMovies() {
        viewModelScope.launchLoadingAndError(
            handleError = { e ->
                updateState(MovieListViewState.State.Error(e.localizedMessage.orEmpty()))
            }
        ) {
            updateState(MovieListViewState.State.Loading)

            interactor.observeHighRatingFirstSettings()
                .onEach { updateState(MovieListViewState.State.Loading) }
                .map { interactor.getMovies(it) }
                .collect { movies ->
                    updateState(MovieListViewState.State.Success(mapToUi(movies)))
                }
        }
    }

    private fun updateState(state: MovieListViewState.State) =
        mutableState.update { it.copy(state = state) }

    private fun mapToUi(movies: List<MovieEntity>): List<MovieUiModel> = movies.map { movie ->
        MovieUiModel(
            id = movie.id,
            name = movie.name,
            alternativeName = movie.alternativeName,
            description = movie.description,
            year = movie.year,
            rating = movie.rating,
            posterUrl = movie.posterUrl,
            genres = movie.genres,
            countries = movie.countries
        )
    }
}