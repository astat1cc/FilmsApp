package com.example.films.data.movie.remote.models

import com.example.films.domain.movie.models.Movie

data class MovieFromListResponse(
    val id: Int,
    val poster_path: String,
    val title: String,
) {

    fun mapToDomain() =
        Movie(id = id, poster_path = poster_path, title = title)
}