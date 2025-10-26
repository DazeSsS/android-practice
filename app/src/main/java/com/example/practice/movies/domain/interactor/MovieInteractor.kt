package com.example.practice.movies.domain.interactor

import com.example.practice.movies.data.repository.MovieRepository

class MovieInteractor(
    private val repository: MovieRepository,
) {
    suspend fun getMovies(highRatingFirst: Boolean) = repository.getMovies(highRatingFirst)

    fun observeHighRatingFirstSettings() = repository.observeHighRatingFirstSettings()

    suspend fun setHighRatingFirstSetting(highRatingFirst: Boolean) =
        repository.setHighRatingFirstSettings(highRatingFirst)
}