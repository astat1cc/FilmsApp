package com.example.films.presentation.screens.favourite

import androidx.lifecycle.*
import com.example.films.domain.movie.usecase.GetSavedMoviesUseCase
import com.example.films.presentation.models.MovieListItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class FavouriteViewModel(
    private val getSavedMoviesUseCase: GetSavedMoviesUseCase
) : ViewModel() {

    private val _savedMovieList: MutableLiveData<List<MovieListItem>> = MutableLiveData()
    val savedMovieList: LiveData<List<MovieListItem>> = _savedMovieList

    fun getSavedFavouriteMovies() {
        viewModelScope.launch(Dispatchers.IO) {
            val savedMovieList = getSavedMoviesUseCase.execute().map { movie ->
                MovieListItem.fromMovieDomain(movie)
            }
            _savedMovieList.postValue(savedMovieList)
        }
    }

    class Factory @Inject constructor(
        private val getSavedMoviesUseCase: GetSavedMoviesUseCase
    ) : ViewModelProvider.Factory {

        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T =
            FavouriteViewModel(getSavedMoviesUseCase) as T
    }
}