package com.example.films.domain.movie.usecase

import com.example.films.domain.movie.MoviesRepository
import javax.inject.Inject

class CheckIfMovieInFavouritesUseCase @Inject constructor(
    private val moviesRepository: MoviesRepository
) {

    suspend fun execute(movieId: Int) = moviesRepository.checkIfMovieInFavourites(movieId)
}