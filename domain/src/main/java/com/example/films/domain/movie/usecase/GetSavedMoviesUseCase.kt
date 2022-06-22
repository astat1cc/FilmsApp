package com.example.films.domain.movie.usecase

import com.example.films.domain.movie.MoviesRepository
import javax.inject.Inject

class GetSavedMoviesUseCase @Inject constructor(
    private val moviesRepository: MoviesRepository
) {

    suspend fun execute() = moviesRepository.getSavedFavouriteMovies()
}