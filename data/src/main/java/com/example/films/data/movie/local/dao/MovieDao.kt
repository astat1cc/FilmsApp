package com.example.films.data.movie.local.dao

import androidx.room.*
import com.example.films.data.movie.local.models.SavedMovieItem

@Dao
interface MovieDao {

    @Insert(entity = SavedMovieItem::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveMovie(savedMovieItem: SavedMovieItem)

    @Query("SELECT * FROM movies")
    suspend fun getSavedMovies(): List<SavedMovieItem>

    @Query("SELECT EXISTS (SELECT id FROM movies WHERE id = :movieId)")
    suspend fun checkIfMovieInFavourites(movieId: Int): Boolean

    @Query("DELETE FROM movies WHERE id = :movieId")
    suspend fun deleteMovie(movieId: Int)
}