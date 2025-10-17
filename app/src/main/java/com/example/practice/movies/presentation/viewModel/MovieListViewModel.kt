package com.example.practice.movies.presentation.viewModel

import androidx.lifecycle.ViewModel
import com.example.practice.MovieDetail
import com.example.practice.movies.presentation.model.MovieListViewState
import com.example.practice.movies.presentation.model.MovieUiModel
import com.example.practice.navigation.Route
import com.example.practice.navigation.TopLevelBackStack
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class MovieListViewModel(
    private val topLevelBackStack: TopLevelBackStack<Route>,
    private val movies: List<MovieUiModel>,
) : ViewModel() {
    private val mutableState = MutableStateFlow(MovieListViewState(movies))
    val state = mutableState.asStateFlow()

    fun showMovieDetail(movie: MovieUiModel) {
        topLevelBackStack.add(MovieDetail(movie))
    }
}