package com.example.films.presentation.screens.main.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.films.Const.BASE_IMAGES_URL
import com.example.films.Const.POSTERS_FILE_SIZE
import com.example.films.databinding.ItemMovieWithoutDescriptionBinding
import com.example.films.presentation.models.MovieListItem
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import java.io.Serializable
import javax.inject.Inject

class MovieListAdapter(
    private val onMovieItemClickListener: (MovieListItem) -> Unit
) : RecyclerView.Adapter<MovieListAdapter.MainMovieViewHolder>() {

    var currentMovieList: List<MovieListItem> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainMovieViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemMovieWithoutDescriptionBinding.inflate(inflater, parent, false)
        return MainMovieViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MainMovieViewHolder, position: Int) {
        val movie = currentMovieList[position]
        with(holder.binding) {
            Glide.with(movieImage).load(
                "${BASE_IMAGES_URL}${POSTERS_FILE_SIZE}${movie.poster_path}"
            ).into(movieImage)
            movieTitle.text = movie.title
            holder.itemView.setOnClickListener { onMovieItemClickListener(movie) }
        }
    }

    override fun getItemCount(): Int = currentMovieList.size

    class MainMovieViewHolder(val binding: ItemMovieWithoutDescriptionBinding) :
        RecyclerView.ViewHolder(binding.root)
}