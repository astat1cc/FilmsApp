package com.example.films.di

import com.example.films.presentation.screens.favourite.FavouriteFragment
import com.example.films.presentation.screens.main.MainFragment
import com.example.films.presentation.screens.movie_details.MovieDetailsFragment
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [DataModule::class, AppModule::class])
interface AppComponent {

    fun inject(fragment: MainFragment)

    fun inject(fragment: MovieDetailsFragment)

    fun inject(fragment: FavouriteFragment)
}
