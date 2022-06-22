package com.example.films.domain.movie.models

data class Actor(
    val id: Int,
    val name: String,
    val order: Int,
    val profile_path: String?
)