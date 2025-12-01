package com.example.movies.presentation.model

import kotlinx.serialization.Serializable

@Serializable
data class MovieUiModel(
    val id: Int,
    val name: String?,
    val alternativeName: String?,
    val description: String?,
    val year: Int?,
    val rating: Float?,
    val posterUrl: String?,
    val genres: List<String> = emptyList(),
    val countries: List<String> = emptyList()
) {
    val isMultipleCountries: Boolean get() = countries.count() > 1
}
