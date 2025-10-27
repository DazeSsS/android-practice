package com.example.practice.movies.presentation.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.practice.movies.domain.interactor.MovieInteractor
import com.example.practice.movies.domain.model.MovieEntity
import com.example.practice.movies.presentation.model.MovieDetailViewState
import com.example.practice.movies.presentation.model.MovieUiModel
import com.example.practice.navigation.Route
import com.example.practice.navigation.TopLevelBackStack
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MovieDetailViewModel(
    private val topLevelBackStack: TopLevelBackStack<Route>,
    private val movie: MovieUiModel,
    private val interactor: MovieInteractor,
) : ViewModel() {
    private val mutableState = MutableStateFlow(MovieDetailViewState(movie))
    val state = mutableState.asStateFlow()

    init {
        viewModelScope.launch {
            mutableState.update {
                it.copy(isFavorite = interactor.isMovieFavorite(movie.id))
            }
        }
    }

    fun onFavoriteChange() {
        mutableState.update { it.copy(isFavorite = !it.isFavorite) }

        if (state.value.isFavorite) {
            viewModelScope.launch {
                interactor.saveFavorite(
                    MovieEntity(
                        id = movie.id,
                        name = movie.name,
                        alternativeName = movie.alternativeName,
                        description = movie.description,
                        year = movie.year,
                        rating = movie.rating,
                        posterUrl = movie.posterUrl,
                        genres = movie.genres,
                        countries = movie.countries,
                    )
                )
            }
        } else {
            viewModelScope.launch {
                interactor.deleteFavorite(movie.id)
            }
        }

    }

    fun onBack() {
        topLevelBackStack.removeLast()
    }
}