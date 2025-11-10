package com.example.practice.movies.presentation.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.practice.MovieDetail
import com.example.practice.MovieSettings
import com.example.practice.core.launchLoadingAndError
import com.example.practice.movies.domain.interactor.MovieInteractor
import com.example.practice.movies.domain.model.MovieEntity
import com.example.practice.movies.presentation.model.BadgeCache
import com.example.practice.movies.presentation.model.state.MovieListViewState
import com.example.practice.movies.presentation.model.MovieUiModel
import com.example.practice.navigation.Route
import com.example.practice.navigation.TopLevelBackStack
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MovieListViewModel(
    private val topLevelBackStack: TopLevelBackStack<Route>,
    private val interactor: MovieInteractor,
    private val favorites: Boolean = false,
    private val badgeCache: BadgeCache,
) : ViewModel() {
    private val mutableState = MutableStateFlow(
        MovieListViewState(badgeCache = badgeCache)
    )
    val state = mutableState.asStateFlow()

    init {
        loadMovies()
        viewModelScope.launch {
            interactor.observeHighRatingFirstSettings().collect { highRatingFirst ->
                badgeCache.setBadgeActive(!highRatingFirst)
            }
        }
    }

    fun onMovieClick(movie: MovieUiModel) {
        topLevelBackStack.add(MovieDetail(movie))
    }

    fun onRetryClick() = loadMovies()

    fun onSettingsClick() {
        topLevelBackStack.add(MovieSettings)
    }

    fun refreshMovies() = loadMovies()

    private fun loadMovies() {
        viewModelScope.launchLoadingAndError(
            handleError = { e ->
                updateState(MovieListViewState.State.Error(e.localizedMessage.orEmpty()))
            }
        ) {
            updateState(MovieListViewState.State.Loading)

            interactor.observeHighRatingFirstSettings()
                .onEach { updateState(MovieListViewState.State.Loading) }
                .map {
                    if (!favorites) {
                        interactor.getMovies(it)
                    } else {
                        interactor.getFavorites()
                    }
                }
                .collect { movies ->
                    updateState(MovieListViewState.State.Success(mapToUi(movies)))
                }
        }
    }

    private fun updateState(state: MovieListViewState.State) =
        mutableState.update { it.copy(listState = state) }

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