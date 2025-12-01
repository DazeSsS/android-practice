package com.example.movies.domain.model

class MovieEntity(
    val id: Int,
    val name: String?,
    val alternativeName: String?,
    val description: String?,
    val year: Int?,
    val rating: Float?,
    val posterUrl: String?,
    val genres: List<String> = emptyList(),
    val countries: List<String> = emptyList(),
)