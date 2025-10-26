package com.example.practice.movies.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import com.example.practice.movies.data.MoviesApi
import com.example.practice.movies.data.mapper.MovieResponseToEntityMapper
import com.example.practice.movies.domain.model.MovieEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class MovieRepository(
    private val api: MoviesApi,
    private val mapper: MovieResponseToEntityMapper,
    private val dataStore: DataStore<Preferences>,
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

    companion object {
        private const val HIGH_RATING_FIRST_KEY = "HIGH_RATING_FIRST_KEY"

    }
}