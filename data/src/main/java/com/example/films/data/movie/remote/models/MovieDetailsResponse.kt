package com.example.films.data.movie.remote.models

import com.example.films.domain.movie.models.Movie

data class MovieDetailsResponse(
    val genres: List<Genre>,
    val id: Int,
    val overview: String,
    val backdrop_path: String,
    val poster_path: Any,
    val release_date: String,
    val runtime: Int,
    val title: String,
    val vote_average: Double,
    val credits: Credits
) {

    fun mapToDomain() = Movie(
        id = id,
        title = title,
        overview = overview,
        backdrop_path = backdrop_path,
        poster_path = poster_path.toString(),
        release_date = release_date,
        runtime = runtime,
        genres = genres.map { it.name },
        vote_average = vote_average,
        actors = credits.cast.map { it.mapToDomain() }
    )
}