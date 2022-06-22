package com.example.films.presentation.models

import com.example.films.domain.movie.models.Movie

data class MovieWithDetails(
    val id: Int,
    val title: String,
    val overview: String?,
    val backdrop_path: String? = null,
    val poster_path: String? = null,
    val release_date: String?,
    val runtime: Int?,
    val genres: List<String>?,
    val vote_average: Double? = null,
) {

    fun toMovieDomain() = Movie(
        id,
        title,
        overview,
        backdrop_path,
        poster_path,
        release_date,
        runtime,
        genres,
        vote_average
    )

    companion object {

        fun fromMovieDomain(movie: Movie) = MovieWithDetails(
            movie.id,
            movie.title,
            movie.overview,
            movie.backdrop_path,
            movie.poster_path,
            movie.release_date,
            movie.runtime,
            movie.genres,
            movie.vote_average
        )
    }
}