package com.example.practice.movies.domain.interactor

import com.example.practice.movies.data.repository.MovieRepository

class MovieInteractor(
    private val movieRepository: MovieRepository,
) {
    suspend fun getMovies() = movieRepository.getMovies()
}