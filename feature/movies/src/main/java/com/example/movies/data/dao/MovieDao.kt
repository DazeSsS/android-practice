package com.example.movies.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.movies.data.entity.MovieDbEntity

@Dao
interface MovieDao {
    @Query("SELECT * FROM MovieDbEntity")
    suspend fun getAll(): List<MovieDbEntity>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(driverDbEntity: MovieDbEntity)

    @Query("SELECT COUNT(*) FROM MovieDbEntity WHERE id = :movieId")
    suspend fun isMovieExists(movieId: Int): Int

    @Query("DELETE FROM MovieDbEntity WHERE id = :movieId")
    suspend fun deleteById(movieId: Int)
}