package com.example.practice.movies.data.model

import androidx.annotation.Keep
import kotlinx.serialization.Serializable

@Keep
@Serializable
class MovieListResponse(
    val docs: List<MovieListDocument>?,
)

@Keep
@Serializable
class MovieListDocument(
    val id: Int,
    val name: String?,
    val alternativeName: String?,
    val description: String?,
    val year: Int?,
    val rating: Rating,
    val poster: PosterModel?,
    val genres: List<GenresModel> = emptyList(),
    val countries: List<CountriesModel> = emptyList()
)

@Keep
@Serializable
class Rating(
    val kp: Float?,
    val imdb: Float?
)

@Keep
@Serializable
class PosterModel(
    val url: String?
)

@Keep
@Serializable
class GenresModel(
    val name: String
)

@Keep
@Serializable
class CountriesModel(
    val name: String
)