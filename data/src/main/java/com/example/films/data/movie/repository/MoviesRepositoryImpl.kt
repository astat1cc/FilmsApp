package com.example.films.data.movie.repository

import android.util.Log
import com.example.films.data.movie.local.dao.MovieDao
import com.example.films.data.movie.local.models.SavedMovieItem
import com.example.films.data.movie.remote.api.MovieApi
import com.example.films.data.movie.remote.models.MovieDetailsResponse
import com.example.films.data.movie.remote.models.MovieListResponse
import com.example.films.domain.common.NetworkResult
import com.example.films.domain.movie.MoviesRepository
import com.example.films.domain.movie.models.Movie
import retrofit2.Response
import java.lang.Exception
import javax.inject.Inject

class MoviesRepositoryImpl @Inject constructor(
    private val movieApi: MovieApi,
    private val movieDao: MovieDao
) : MoviesRepository {

    override suspend fun getPopularMovieList(): NetworkResult<List<Movie>> {
        return try {
            val response = movieApi.getPopularMovieList()
            handleMovieListResponse(response)
        } catch (e: Exception) {
            NetworkResult.Error(e.message ?: "")
        }
    }

    override suspend fun getTopRatedMovieList(): NetworkResult<List<Movie>> {
        return try {
            val response = movieApi.getTopRatedMovieList()
            handleMovieListResponse(response)
        } catch (e: Exception) {
            NetworkResult.Error(e.message ?: "")
        }
    }

    override suspend fun getUpcomingMovieList(): NetworkResult<List<Movie>> {
        return try {
            val response = movieApi.getUpcomingMovieList()
            handleMovieListResponse(response)
        } catch (e: Exception) {
            NetworkResult.Error(e.message ?: "")
        }
    }

    override suspend fun getMovieDetails(id: Int): NetworkResult<Movie> {
        return try {
            val response = movieApi.getMovieDetails(id)
            handleMovieDetailsResponse(response)
        } catch (e: Exception) {
            NetworkResult.Error(e.message ?: "")
        }
    }

    override suspend fun saveMovieToFavourites(movie: Movie) {
        movieDao.saveMovie(
            SavedMovieItem.fromMovie(movie)
        )
    }

    override suspend fun deleteMovieFromFavourites(movieId: Int) {
        movieDao.deleteMovie(movieId)
    }

    override suspend fun getSavedFavouriteMovies(): List<Movie> =
        movieDao.getSavedMovies().map { it.mapToDomain() }

    override suspend fun checkIfMovieInFavourites(movieId: Int) =
        movieDao.checkIfMovieInFavourites(movieId)

    private fun handleMovieListResponse(response: Response<MovieListResponse>): NetworkResult<List<Movie>> {
        return if (response.isSuccessful && response.body() != null) {
            val movieList = response.body()!!.results.map { movieResponse ->
                movieResponse.mapToDomain()
            }
            NetworkResult.Success(movieList)
        } else {
            NetworkResult.Error(response.message())
        }
    }

    private fun handleMovieDetailsResponse(
        response: Response<MovieDetailsResponse>
    ): NetworkResult<Movie> {
        return if (response.isSuccessful && response.body() != null) {
            Log.d("cast", "${response.body()}")
            val movie = response.body()!!.mapToDomain()
            NetworkResult.Success(movie)
        } else {
            NetworkResult.Error(response.message())
        }
    }
}