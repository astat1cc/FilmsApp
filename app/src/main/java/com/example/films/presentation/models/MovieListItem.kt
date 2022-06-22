package com.example.films.presentation.models

import android.os.Parcelable
import com.example.films.domain.movie.models.Movie
import kotlinx.parcelize.Parcelize
import java.io.Serializable

@Parcelize
data class MovieListItem(
    val id: Int,
    val poster_path: String?,
    val title: String,
    val overview: String? = null
) : Parcelable {

    fun toMovieDomain() =
        Movie(
            id = id,
            poster_path = poster_path,
            title = title,
            overview = overview
        )

    companion object {
        fun fromMovieDomain(movie: Movie) = MovieListItem(
            id = movie.id,
            poster_path = movie.poster_path,
            title = movie.title,
            overview = movie.overview
        )
    }
}