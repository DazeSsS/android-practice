package com.example.practice.movies.data.repository

import com.example.practice.movies.data.MoviesApi
import com.example.practice.movies.data.mapper.MovieResponseToEntityMapper
import com.example.practice.movies.domain.model.MovieEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class MovieRepository(
    private val api: MoviesApi,
    private val mapper: MovieResponseToEntityMapper
) {
    suspend fun getMovies(): List<MovieEntity> = withContext(Dispatchers.IO) {
        val response = api.getMovies()
        mapper.mapResponse(response)
    }
}