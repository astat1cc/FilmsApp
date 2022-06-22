package com.example.films.data.movie.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.films.data.movie.local.dao.MovieDao
import com.example.films.data.movie.local.models.SavedMovieItem

@Database(
    entities = [SavedMovieItem::class],
    version = 1
)
abstract class MoviesRoomDatabase : RoomDatabase() {

    abstract fun movieDao(): MovieDao
}