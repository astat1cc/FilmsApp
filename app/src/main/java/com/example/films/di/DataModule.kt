package com.example.films.di

import com.example.films.MainApp
import com.example.films.data.movie.local.dao.MovieDao
import com.example.films.data.movie.remote.api.MovieApi
import com.example.films.data.movie.repository.MoviesRepositoryImpl
import com.example.films.domain.movie.MoviesRepository
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module(includes = [RoomModule::class, RetrofitModule::class])
class DataModule {

    @Singleton
    @Provides
    fun provideMoviesRepository(movieApi: MovieApi, movieDao: MovieDao): MoviesRepository =
        MoviesRepositoryImpl(movieApi, movieDao)


//    @Binds
//    abstract fun bindMovieRepositoryImpl_to_MovieRepository(moviesRepositoryImpl: MoviesRepositoryImpl): MoviesRepository
}