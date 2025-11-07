package com.example.practice.movies.data.api

import com.example.practice.movies.data.model.MovieListResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface MoviesApi {
    @GET("movie")
    suspend fun getMovies(
        @Query("sortField") sortField: String = RATING_SORT_FIELD,
        @Query(RATING_SORT_FIELD) ratingRange: String = RATING_RANGE,
        @Query("sortType") sortType: Int,
    ): MovieListResponse

    companion object {
        private const val RATING_SORT_FIELD: String = "rating.kp"
        private const val RATING_RANGE: String = "0.1-10"
    }
}