package com.example.practice.movies.data

import com.example.practice.movies.data.model.MovieListResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface MoviesApi {
    @GET("movie")
    suspend fun getMovies(
        @Query("sortField") sortField: String = SORT_FIELD,
        @Query("sortType") sortType: Int = SORT_TYPE,
    ): MovieListResponse

    companion object {
        private const val SORT_FIELD: String = "rating.kp"
        private const val SORT_TYPE: Int = -1
    }
}