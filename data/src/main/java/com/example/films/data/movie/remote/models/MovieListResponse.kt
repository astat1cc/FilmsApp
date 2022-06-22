package com.example.films.data.movie.remote.models

data class MovieListResponse(
    val page: Int,
    val results: List<MovieFromListResponse>,
    val total_pages: Int,
    val total_results: Int
)