package com.example.films.data.movie.remote.models

import com.example.films.domain.movie.models.Actor

data class ActorResponse(
    val id: Int,
    val name: String,
    val order: Int,
    val profile_path: String? = null
) {

    fun mapToDomain() = Actor(id = id, name = name, order = order, profile_path = profile_path)
}