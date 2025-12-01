package com.example.movies.data.mapper

import com.example.movies.data.model.MovieListResponse
import com.example.movies.domain.model.MovieEntity

class MovieResponseToEntityMapper {
    fun mapResponse(response: MovieListResponse): List<MovieEntity> {
        return response.docs?.map { doc ->
            MovieEntity(
                id = doc.id,
                name = doc.name,
                alternativeName = doc.alternativeName,
                description = doc.description,
                year = doc.year,
                rating = doc.rating.kp,
                posterUrl = doc.poster?.url,
                genres = doc.genres.map { it.name },
                countries = doc.countries.map { it.name }
            )
        }.orEmpty()
    }
}