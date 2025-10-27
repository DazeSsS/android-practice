package com.example.practice.movies.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.example.practice.movies.data.dao.MovieDao
import com.example.practice.movies.data.entity.MovieDbEntity

@Database(entities = [MovieDbEntity::class], version = 1)
@TypeConverters(Converters::class)
abstract class MovieDatabase : RoomDatabase() {
    abstract fun movieDao(): MovieDao
}

class Converters {

    @TypeConverter
    fun fromStringList(list: List<String>): String {
        return if (list.isEmpty()) ""
        else list.joinToString(separator = "||")
    }

    @TypeConverter
    fun toStringList(data: String): List<String> {
        return if (data.isBlank()) emptyList()
        else data.split("||")
    }
}