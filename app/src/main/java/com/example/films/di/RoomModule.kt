package com.example.films.di

import android.content.Context
import androidx.room.Room
import com.example.films.data.movie.local.MoviesRoomDatabase
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class RoomModule(val appContext: Context) {

    @Singleton
    @Provides
    fun provideMovieRoomDatabase() =
        Room.databaseBuilder(
            appContext,
            MoviesRoomDatabase::class.java,
            "movies_database"
        ).build()

    @Singleton
    @Provides
    fun provideMovieDao(database: MoviesRoomDatabase) = database.movieDao()
}