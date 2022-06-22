package com.example.films.domain.movie.usecase

import com.example.films.domain.movie.MoviesRepository
import javax.inject.Inject

class GetMovieDetailsUseCase @Inject constructor(
    private val moviesRepository: MoviesRepository
) {

    suspend fun execute(id: Int) = moviesRepository.getMovieDetails(id)
}