package com.example.practice.movies.presentation.model

data class MovieSettingsState(
    val highRatingFirst: Boolean = true,
    val badgeCache: BadgeCache? = null,
)
