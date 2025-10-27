package com.example.practice.movies.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.practice.movies.data.db.Converters

@Entity
class MovieDbEntity(
    @PrimaryKey() val id: Int,
    @ColumnInfo(name = "name") val name: String?,
    @ColumnInfo(name = "alternativeName") val alternativeName: String?,
    @ColumnInfo(name = "description") val description: String?,
    @ColumnInfo(name = "year") val year: Int?,
    @ColumnInfo(name = "rating") val rating: Float?,
    @ColumnInfo(name = "posterUrl") val posterUrl: String?,

    @ColumnInfo(name = "genres")
    @field:TypeConverters(Converters::class)
    val genres: List<String>,

    @ColumnInfo(name = "countries")
    @field:TypeConverters(Converters::class)
    val countries: List<String>,
)