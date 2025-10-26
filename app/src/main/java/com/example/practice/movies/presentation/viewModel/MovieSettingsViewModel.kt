package com.example.practice.movies.presentation.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.practice.movies.domain.interactor.MovieInteractor
import com.example.practice.movies.presentation.model.MovieSettingsState
import com.example.practice.navigation.Route
import com.example.practice.navigation.TopLevelBackStack
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MovieSettingsViewModel(
    private val topLevelBackStack: TopLevelBackStack<Route>,
    private val interactor: MovieInteractor,
): ViewModel() {
    private val mutableState = MutableStateFlow(MovieSettingsState())
    val state = mutableState.asStateFlow()

    init {
        viewModelScope.launch {
            interactor.observeHighRatingFirstSettings().collect { highRatingFirst ->
                mutableState.update { it.copy(highRatingFirst = highRatingFirst) }
            }
        }
    }

    fun onHighRatingFirstCheckedChange(isChecked: Boolean) {
        mutableState.update { it.copy(highRatingFirst = isChecked) }
    }

    fun onBack() {
        topLevelBackStack.removeLast()
    }

    fun onSaveClicked() {
        viewModelScope.launch {
            interactor.setHighRatingFirstSetting(state.value.highRatingFirst)
            onBack()
        }
    }
}