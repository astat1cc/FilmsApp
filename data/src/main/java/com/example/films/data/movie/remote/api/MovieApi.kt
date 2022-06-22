package com.example.films.data.movie.remote.api

import com.example.films.data.movie.remote.models.MovieDetailsResponse
import com.example.films.data.movie.remote.models.MovieListResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

private const val API_KEY = "0e0d58435bbf5467cb8c85406ce3d2c9"

interface MovieApi {

    @GET("./movie/popular")
    suspend fun getPopularMovieList(
        @Query("api_key")
        apiKey: String = API_KEY,

        @Query("page")
        page: Int = 1
    ): Response<MovieListResponse>

    @GET("./movie/top_rated")
    suspend fun getTopRatedMovieList(
        @Query("api_key")
        apiKey: String = API_KEY,

        @Query("page")
        page: Int = 1
    ) : Response<MovieListResponse>

    @GET("./movie/upcoming")
    suspend fun getUpcomingMovieList(
        @Query("api_key")
        apiKey: String = API_KEY,

        @Query("page")
        page: Int = 1
    ) : Response<MovieListResponse>

    @GET("/3/movie/{movie_id}")
    suspend fun getMovieDetails(
        @Path("movie_id") id: Int,

        @Query("api_key")
        apiKey: String = API_KEY,

        @Query("append_to_response")
        appendToResponse: String = "credits"
    ) : Response<MovieDetailsResponse>
}