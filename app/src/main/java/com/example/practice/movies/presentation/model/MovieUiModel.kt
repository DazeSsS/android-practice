package com.example.practice.movies.presentation.model

import kotlinx.serialization.Serializable

@Serializable
data class MovieUiModel(
    val id: Int,
    val name: String?,
    val alternativeName: String?,
    val description: String?,
    val year: Int?,
    val posterUrl: String?,
    val genres: List<GenreUiModel> = emptyList(),
    val countries: List<CountryUiModel> = emptyList()
) {
    val isMultipleCountries: Boolean get() = countries.count() > 1
}
