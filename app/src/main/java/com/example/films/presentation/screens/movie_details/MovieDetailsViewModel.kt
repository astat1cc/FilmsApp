package com.example.films.presentation.screens.movie_details

import android.util.Log
import androidx.lifecycle.*
import com.example.films.domain.common.NetworkResult
import com.example.films.domain.movie.usecase.CheckIfMovieInFavouritesUseCase
import com.example.films.domain.movie.usecase.DeleteMovieFromFavouritesUseCase
import com.example.films.domain.movie.usecase.GetMovieDetailsUseCase
import com.example.films.domain.movie.usecase.SaveMovieToFavouritesUseCase
import com.example.films.presentation.models.ItemActor
import com.example.films.presentation.models.MovieWithDetails
import com.example.films.presentation.screens.common.UiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class MovieDetailsViewModel @Inject constructor(
    private val getMovieDetailsUseCase: GetMovieDetailsUseCase,
    private val saveMovieToFavouritesUseCase: SaveMovieToFavouritesUseCase,
    private val checkIfMovieInFavouritesUseCase: CheckIfMovieInFavouritesUseCase,
    private val deleteMovieFromFavouritesUseCase: DeleteMovieFromFavouritesUseCase
) : ViewModel() {

    private val _uiState: MutableLiveData<UiState> = MutableLiveData(UiState.Loading)
    val uiState: LiveData<UiState> = _uiState

    private val _isMovieInFavourites: MutableLiveData<Boolean> = MutableLiveData()
    val isMovieInFavourites: LiveData<Boolean> = _isMovieInFavourites

    private val _actors: MutableLiveData<List<ItemActor>> = MutableLiveData()
    val actors: LiveData<List<ItemActor>> = _actors

    private val _movie: MutableLiveData<MovieWithDetails> = MutableLiveData()
    val movie: LiveData<MovieWithDetails> = _movie

    fun getMovieDetails(id: Int) {
        _uiState.postValue(UiState.Loading)
        viewModelScope.launch(Dispatchers.IO) {
            val result = getMovieDetailsUseCase.execute(id)
            when (result) {
                is NetworkResult.Success -> {
                    val movie = MovieWithDetails.fromMovieDomain(result.data)
                    _movie.postValue(movie)
                    result.data.actors?.let { actorsDomain ->
                        val actors = actorsDomain
                            .map { ItemActor.fromActorDomain(it) }
                            .sortedBy { it.order }
                            .take(10)
                        _actors.postValue(actors)
                    }
                    _uiState.postValue(UiState.LoadSuccess)
                }
                else -> {
                    _uiState.postValue(UiState.LoadError)
                }
            }
        }
    }

    fun posterLoaded() {
        _uiState.postValue(UiState.LoadSuccess)
        Log.d("test", "poster loaded")
    }

    fun saveMovie() {
        viewModelScope.launch(Dispatchers.IO) {
            movie.value?.let {
                saveMovieToFavouritesUseCase.execute(it.toMovieDomain())
                _isMovieInFavourites.postValue(true)
            }
        }
    }

    fun deleteMovie() {
        viewModelScope.launch(Dispatchers.IO) {
            movie.value?.let {
                deleteMovieFromFavouritesUseCase.execute(it.id)
                _isMovieInFavourites.postValue(false)
            }
        }
    }

    fun checkIfMovieInFavourites(movieId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            _isMovieInFavourites.postValue(
                checkIfMovieInFavouritesUseCase.execute(movieId)
            )
        }
    }

    class Factory @Inject constructor(
        private val getMovieDetailsUseCase: GetMovieDetailsUseCase,
        private val saveMovieToFavouritesUseCase: SaveMovieToFavouritesUseCase,
        private val checkIfMovieInFavouritesUseCase: CheckIfMovieInFavouritesUseCase,
        private val deleteMovieFromFavouritesUseCase: DeleteMovieFromFavouritesUseCase
    ) : ViewModelProvider.Factory {

        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T =
            MovieDetailsViewModel(
                getMovieDetailsUseCase,
                saveMovieToFavouritesUseCase,
                checkIfMovieInFavouritesUseCase,
                deleteMovieFromFavouritesUseCase
            ) as T
    }
}
