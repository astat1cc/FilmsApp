package com.example.films.domain.movie.usecase

import com.example.films.domain.movie.MoviesRepository
import com.example.films.domain.movie.models.Movie
import javax.inject.Inject

class SaveMovieToFavouritesUseCase @Inject constructor(
    private val moviesRepository: MoviesRepository
) {

    suspend fun execute(movie: Movie) = moviesRepository.saveMovieToFavourites(movie)
}