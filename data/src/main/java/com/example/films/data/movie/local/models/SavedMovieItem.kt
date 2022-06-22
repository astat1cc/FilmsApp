package com.example.films.data.movie.local.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.films.domain.movie.models.Movie

@Entity(tableName = "movies")
data class SavedMovieItem(
    @PrimaryKey val id: Int,
    val poster_path: String?,
    val title: String,
    val overview: String? = null
) {

    fun mapToDomain() =
        Movie(
            id = id,
            poster_path = poster_path,
            title = title,
            overview = overview
        )

    companion object {
        fun fromMovie(movie: Movie) = SavedMovieItem(
            id = movie.id,
            poster_path = movie.poster_path,
            title = movie.title,
            overview = movie.overview
        )
    }
}