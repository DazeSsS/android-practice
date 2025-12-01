package com.example.movies.presentation.model.state

import com.example.movies.presentation.model.BadgeCache

data class MovieSettingsState(
    val highRatingFirst: Boolean = true,
    val badgeCache: BadgeCache? = null,
)
