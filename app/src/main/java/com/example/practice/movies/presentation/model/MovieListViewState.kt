package com.example.practice.movies.presentation.model

data class MovieListViewState(
    val state: State = State.Loading,
) {
    sealed interface State {
        object Loading : State
        data class Error(val error: String) : State
        data class Success(val data: List<MovieUiModel>) : State
    }
}
