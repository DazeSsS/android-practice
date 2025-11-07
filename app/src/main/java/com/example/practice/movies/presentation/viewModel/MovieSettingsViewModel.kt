package com.example.practice.movies.presentation.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.practice.movies.domain.interactor.MovieInteractor
import com.example.practice.movies.presentation.model.BadgeCache
import com.example.practice.movies.presentation.model.MovieSettingsState
import com.example.practice.navigation.Route
import com.example.practice.navigation.TopLevelBackStack
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MovieSettingsViewModel(
    private val topLevelBackStack: TopLevelBackStack<Route>,
    private val interactor: MovieInteractor,
    private val badgeCache: BadgeCache,
): ViewModel() {
    private val mutableState = MutableStateFlow(
        MovieSettingsState(badgeCache = badgeCache)
    )
    val state = mutableState.asStateFlow()

    init {
        viewModelScope.launch {
            interactor.observeHighRatingFirstSettings().collect { highRatingFirst ->
                mutableState.update { it.copy(highRatingFirst = highRatingFirst) }
                badgeCache.setBadgeActive(!highRatingFirst)
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
            badgeCache.setBadgeActive(!state.value.highRatingFirst)
            onBack()
        }
    }
}