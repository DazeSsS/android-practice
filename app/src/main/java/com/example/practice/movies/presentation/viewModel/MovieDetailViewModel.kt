package com.example.practice.movies.presentation.viewModel

import androidx.lifecycle.ViewModel
import com.example.practice.movies.presentation.model.MovieDetailViewState
import com.example.practice.movies.presentation.model.MovieUiModel
import com.example.practice.navigation.Route
import com.example.practice.navigation.TopLevelBackStack
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class MovieDetailViewModel(
    private val topLevelBackStack: TopLevelBackStack<Route>,
    private val movie: MovieUiModel,
) : ViewModel() {
    private val mutableState = MutableStateFlow(MovieDetailViewState(movie))
    val state = mutableState.asStateFlow()

    fun onBack() {
        topLevelBackStack.removeLast()
    }
}