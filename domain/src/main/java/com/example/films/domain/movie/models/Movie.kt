package com.example.films.domain.movie.models

data class Movie(
    val id: Int,
    val title: String,
    val overview: String? = null,
    val backdrop_path: String? = null,
    val poster_path: String? = null,
    val release_date: String? = null,
    val runtime: Int? = null,
    val genres: List<String>? = null,
    val vote_average: Double? = null,
    val actors: List<Actor>? = null
)