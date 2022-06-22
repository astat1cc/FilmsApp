package com.example.films.presentation.screens.main

import android.util.Log
import androidx.lifecycle.*
import com.example.films.Const
import com.example.films.domain.common.NetworkResult
import com.example.films.domain.movie.usecase.GetPopularMovieListUseCase
import com.example.films.domain.movie.usecase.GetTopRatedMovieListUseCase
import com.example.films.domain.movie.usecase.GetUpcomingMovieListUseCase
import com.example.films.presentation.models.MovieListItem
import com.example.films.presentation.screens.common.UiState
import com.example.films.presentation.screens.main.models.MovieCategory
import kotlinx.coroutines.*
import java.lang.Exception
import javax.inject.Inject

class MainViewModel(
    private val getPopularMovieListUseCase: GetPopularMovieListUseCase,
    private val getTopRatedMovieListUseCase: GetTopRatedMovieListUseCase,
    private val getUpcomingMovieListUseCase: GetUpcomingMovieListUseCase,
) : ViewModel() {

    private val _scrollPositions: MutableLiveData<MutableMap<Int, Int>> = MutableLiveData(
        mutableMapOf(
            Const.POPULAR_MOVIES_ID to 0,
            Const.TOP_RATED_MOVIES_ID to 0,
            Const.UPCOMING_MOVIES_ID to 0
        )
    )
    val scrollPositions: LiveData<MutableMap<Int, Int>> = _scrollPositions

    private val _uiState: MutableLiveData<UiState> = MutableLiveData(UiState.Loading)
    val uiState: LiveData<UiState> = _uiState

    private val _movieCategoryList: MutableLiveData<List<MovieCategory>> = MutableLiveData(
        listOf(
            MovieCategory(
                Const.POPULAR_MOVIES_ID,
                Const.POPULAR_MOVIES_TITLE,
                emptyList()
            ),
            MovieCategory(
                Const.TOP_RATED_MOVIES_ID,
                Const.TOP_RATED_MOVIES_TITLE,
                emptyList()
            ),
            MovieCategory(
                Const.UPCOMING_MOVIES_ID,
                Const.UPCOMING_MOVIES_TITLE,
                emptyList()
            )
        )
    )
    val movieCategoryList: LiveData<List<MovieCategory>> = _movieCategoryList

    private val _popularMovieList = MutableLiveData<List<MovieListItem>>()
    val popularMovieList: LiveData<List<MovieListItem>> = _popularMovieList

    private val _topRatedMovieList = MutableLiveData<List<MovieListItem>>()
    val topRatedMovieList: LiveData<List<MovieListItem>> = _topRatedMovieList

    private val _upcomingMovieList = MutableLiveData<List<MovieListItem>>()
    val upcomingMovieList: LiveData<List<MovieListItem>> = _upcomingMovieList

    fun getAllMovies() {
        if (_uiState.value == UiState.LoadSuccess) return

        _uiState.postValue(UiState.Loading)

        viewModelScope.launch {
            val popular = launch { getPopularMovies() }
            val topRated = launch { getTopRatedMovies() }
            val upcoming = launch { getUpcomingMovies() }
            joinAll(popular, topRated, upcoming)
            if (_uiState.value != UiState.LoadError) {
                _uiState.postValue(UiState.LoadSuccess)
            }
        }

    }

    private suspend fun getPopularMovies() = withContext(Dispatchers.IO) {
        val result = getPopularMovieListUseCase.execute()
        if (result is NetworkResult.Success) {
            _popularMovieList.postValue(
                result.data.map { MovieListItem.fromMovieDomain(it) }
            )
        } else {
            _uiState.postValue(UiState.LoadError)
        }
    }

    private suspend fun getTopRatedMovies() = withContext(Dispatchers.IO) {
        val result = getTopRatedMovieListUseCase.execute()
        if (result is NetworkResult.Success) {
            _topRatedMovieList.postValue(
                result.data.map { MovieListItem.fromMovieDomain(it) }
            )
        } else {
            _uiState.postValue(UiState.LoadError)
        }
    }

    private suspend fun getUpcomingMovies() = withContext(Dispatchers.IO) {
        val result = getUpcomingMovieListUseCase.execute()
        if (result is NetworkResult.Success) {
            _upcomingMovieList.postValue(
                result.data.map { MovieListItem.fromMovieDomain(it) }
            )
        } else {
            _uiState.postValue(UiState.LoadError)
        }
    }

    fun updateMovieList(newMovieList: List<MovieListItem>, categoryId: Int) {
        _movieCategoryList.value?.let { categories ->
            categories.find { it.id == categoryId }?.let { category ->
                category.movieList = newMovieList
            }
            _movieCategoryList.postValue(categories)
        }
    }

    fun saveScrollPosition(movieCategoryId: Int, position: Int) {
        _scrollPositions.value?.apply {
            this[movieCategoryId] = position
            _scrollPositions.postValue(this)
        }
    }

    class Factory @Inject constructor(
        private val getPopularMovieListUseCase: GetPopularMovieListUseCase,
        private val getTopRatedMovieListUseCase: GetTopRatedMovieListUseCase,
        private val getUpcomingMovieListUseCase: GetUpcomingMovieListUseCase,
    ) : ViewModelProvider.Factory {

        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T =
            MainViewModel(
                getPopularMovieListUseCase = getPopularMovieListUseCase,
                getTopRatedMovieListUseCase = getTopRatedMovieListUseCase,
                getUpcomingMovieListUseCase = getUpcomingMovieListUseCase
            ) as T
    }
}