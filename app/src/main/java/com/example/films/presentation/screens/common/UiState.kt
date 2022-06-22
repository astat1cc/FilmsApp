package com.example.films.presentation.screens.common

sealed class UiState {

    object LoadSuccess : UiState()

    object Loading : UiState()

    object LoadError : UiState()
}


