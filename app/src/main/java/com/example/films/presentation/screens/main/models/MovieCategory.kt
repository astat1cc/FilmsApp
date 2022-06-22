package com.example.films.presentation.screens.main.models

import com.example.films.presentation.models.MovieListItem
import java.io.Serializable

data class MovieCategory(
    val id: Int,
    val title: String,
    var movieList: List<MovieListItem>,
    val loadFailed: Boolean = false
)