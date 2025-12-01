package com.example.movies.domain.interactor

import com.example.movies.data.repository.MovieRepository
import com.example.movies.domain.model.MovieEntity

class MovieInteractor(
    private val repository: MovieRepository,
) {
    suspend fun getMovies(highRatingFirst: Boolean) = repository.getMovies(highRatingFirst)

    fun observeHighRatingFirstSettings() = repository.observeHighRatingFirstSettings()

    suspend fun setHighRatingFirstSetting(highRatingFirst: Boolean) =
        repository.setHighRatingFirstSettings(highRatingFirst)

    suspend fun saveFavorite(movie: MovieEntity) = repository.saveFavorite(movie)

    suspend fun isMovieFavorite(movieId: Int) = repository.isMovieExists(movieId) != 0

    suspend fun getFavorites() = repository.getFavorites()

    suspend fun deleteFavorite(movieId: Int) = repository.deleteFavorite(movieId)
}