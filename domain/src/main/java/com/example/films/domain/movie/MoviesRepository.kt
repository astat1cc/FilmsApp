package com.example.films.domain.movie

import com.example.films.domain.common.NetworkResult
import com.example.films.domain.movie.models.Actor
import com.example.films.domain.movie.models.Movie

interface MoviesRepository {

    suspend fun getPopularMovieList(): NetworkResult<List<Movie>>

    suspend fun getTopRatedMovieList(): NetworkResult<List<Movie>>

    suspend fun getUpcomingMovieList(): NetworkResult<List<Movie>>

    suspend fun getMovieDetails(id: Int) : NetworkResult<Movie>

    suspend fun saveMovieToFavourites(movie: Movie)

    suspend fun deleteMovieFromFavourites(movieId: Int)

    suspend fun getSavedFavouriteMovies(): List<Movie>

    suspend fun checkIfMovieInFavourites(movieId: Int): Boolean
}