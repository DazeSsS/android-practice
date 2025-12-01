package com.example.movies.presentation.model.state

import com.example.movies.presentation.model.MovieUiModel

data class MovieDetailViewState(
    val movie: MovieUiModel,
    val isFavorite: Boolean = false,
)
