package com.example.movies.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import com.example.movies.data.api.MoviesApi
import com.example.movies.data.db.MovieDatabase
import com.example.movies.data.entity.MovieDbEntity
import com.example.movies.data.mapper.MovieResponseToEntityMapper
import com.example.movies.domain.model.MovieEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class MovieRepository(
    private val api: MoviesApi,
    private val mapper: MovieResponseToEntityMapper,
    private val dataStore: DataStore<Preferences>,
    private val db: MovieDatabase,
) {
    private val highRatingFirstKey = booleanPreferencesKey(HIGH_RATING_FIRST_KEY)

    suspend fun getMovies(highRatingFirst: Boolean): List<MovieEntity> = withContext(Dispatchers.IO) {
        val response = api.getMovies(sortType = if (highRatingFirst) -1 else 1)
        mapper.mapResponse(response)
    }

    suspend fun setHighRatingFirstSettings(highRatingFirst: Boolean) = withContext(Dispatchers.IO) {
        dataStore.edit {
            it[highRatingFirstKey] = highRatingFirst
        }
    }

    fun observeHighRatingFirstSettings(): Flow<Boolean> =
        dataStore.data.map { it[highRatingFirstKey] ?: true }

    suspend fun getFavorites() =
        withContext(Dispatchers.IO) {
            db.movieDao().getAll().map {
                MovieEntity(
                    id = it.id,
                    name = it.name,
                    alternativeName = it.alternativeName,
                    description = it.description,
                    year = it.year,
                    rating = it.rating,
                    posterUrl = it.posterUrl,
                    genres = it.genres,
                    countries = it.countries
                )
            }
        }

    suspend fun isMovieExists(movieId: Int): Int =
        withContext(Dispatchers.IO) {
            db.movieDao().isMovieExists(movieId)
        }

    suspend fun saveFavorite(movie: MovieEntity) =
        withContext(Dispatchers.IO) {
            db.movieDao().insert(
                MovieDbEntity(
                    id = movie.id,
                    name = movie.name,
                    alternativeName = movie.alternativeName,
                    description = movie.description,
                    year = movie.year,
                    rating = movie.rating,
                    posterUrl = movie.posterUrl,
                    genres = movie.genres,
                    countries = movie.countries
                )
            )
        }

    suspend fun deleteFavorite(movieId: Int) =
        withContext(Dispatchers.IO) {
            db.movieDao().deleteById(movieId)
        }

    companion object {
        private const val HIGH_RATING_FIRST_KEY = "HIGH_RATING_FIRST_KEY"
    }
}