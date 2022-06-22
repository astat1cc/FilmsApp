package com.example.films.presentation.models

import com.example.films.domain.movie.models.Actor

data class ItemActor(
    val id: Int,
    val name: String,
    val order: Int,
    val profile_path: String?
) {

    fun toActorDomain() = Actor(id = id, name = name, order = order, profile_path = profile_path)

    companion object {
        fun fromActorDomain(actor: Actor) =
            ItemActor(
                id = actor.id,
                name = actor.name,
                order = actor.order,
                profile_path = actor.profile_path
            )
    }
}