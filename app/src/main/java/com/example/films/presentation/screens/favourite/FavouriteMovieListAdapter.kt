package com.example.films.presentation.screens.favourite

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.films.Const
import com.example.films.databinding.ItemMovieWithDescriptionBinding
import com.example.films.presentation.models.MovieListItem

class FavouriteMovieListAdapter(
    private val onMovieItemClickListener: (MovieListItem) -> Unit
) : RecyclerView.Adapter<FavouriteMovieListAdapter.MovieViewHolder>() {

    class MovieViewHolder(val binding: ItemMovieWithDescriptionBinding) : RecyclerView.ViewHolder(binding.root)

    var currentList: List<MovieListItem>
        get() = differ.currentList
        set(value) = differ.submitList(value)

    private val diffCallback = object : DiffUtil.ItemCallback<MovieListItem>() {
        override fun areItemsTheSame(oldItem: MovieListItem, newItem: MovieListItem): Boolean =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: MovieListItem, newItem: MovieListItem): Boolean =
            oldItem == newItem
    }

    private val differ = AsyncListDiffer(this, diffCallback)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MovieViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemMovieWithDescriptionBinding.inflate(inflater, parent, false)
        return MovieViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        val movie = differ.currentList[position]
        with(holder.binding) {
            Glide.with(movieImage).load(
                "${Const.BASE_IMAGES_URL}${Const.POSTERS_FILE_SIZE}${movie.poster_path}"
            ).into(movieImage)
            titleTextView.text = movie.title
            descriptionTextView.text = movie.overview
            holder.itemView.setOnClickListener { onMovieItemClickListener(movie) }
        }
    }

    override fun getItemCount(): Int = differ.currentList.size
}