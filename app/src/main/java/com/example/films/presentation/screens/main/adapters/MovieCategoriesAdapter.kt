package com.example.films.presentation.screens.main.adapters

import android.content.Context
import android.view.Gravity
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.SCROLL_STATE_IDLE
import com.example.films.databinding.ItemMovieCategoryBinding
import com.example.films.presentation.models.MovieListItem
import com.example.films.presentation.screens.main.models.MovieCategory
import com.github.rubensousa.gravitysnaphelper.GravitySnapHelper
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import java.io.Serializable

typealias MovieCategoryId = Int
typealias FirstVisibleItemPosition = Int

class MovieCategoriesAdapter(
    private val onMovieItemClickListener: (MovieListItem) -> Unit,
    private val onSaveScrollPositionListener: (MovieCategoryId, FirstVisibleItemPosition) -> Unit
) : RecyclerView.Adapter<MovieCategoriesAdapter.MovieCategoryViewHolder>() {

    var currentCategoryList: List<MovieCategory> = emptyList()
        set(value) {
            val diffCallback = MovieCategoryDiffCallback(field, value)
            val diffResult = DiffUtil.calculateDiff(diffCallback)
            field = value
            diffResult.dispatchUpdatesTo(this)
        }

    lateinit var innerRecyclerViewSavedScrollPositions: MutableMap<Int, Int>

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieCategoryViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemMovieCategoryBinding.inflate(inflater, parent, false)
        return MovieCategoryViewHolder(binding, onMovieItemClickListener, parent.context)
    }

    override fun onBindViewHolder(holder: MovieCategoryViewHolder, position: Int) {
        val movieCategory = currentCategoryList[position]
        val categoryId = movieCategory.id
        with(holder.binding) {
            categoryTitleTextView.text = movieCategory.title
            movieListRecyclerView.apply {
                adapter = holder.adapter.apply {
                    if (currentMovieList.isEmpty()) {
                        currentMovieList = movieCategory.movieList
                    }
                }

                layoutManager = holder.layoutManager

                holder.snapHelper.attachToRecyclerView(this)

                addOnScrollListener(createSavingScrollPositionScrollListener(categoryId))

                scrollToPosition(innerRecyclerViewSavedScrollPositions[categoryId]!!)
            }
        }
    }

    private fun createSavingScrollPositionScrollListener(categoryId: Int) =
        object : RecyclerView.OnScrollListener() {

            override fun onScrollStateChanged(
                recyclerView: RecyclerView,
                newState: Int
            ) {
                super.onScrollStateChanged(recyclerView, newState)

                if (newState != SCROLL_STATE_IDLE) return
                val firstVisibleItemPosition =
                    (recyclerView.layoutManager as LinearLayoutManager).findFirstCompletelyVisibleItemPosition()
                saveScrollPosition(categoryId, firstVisibleItemPosition)
            }
        }

    private fun saveScrollPosition(movieCategoryId: Int, firstVisibleItemPosition: Int) {
        onSaveScrollPositionListener(movieCategoryId, firstVisibleItemPosition)
    }

    override fun getItemCount(): Int = currentCategoryList.size

    class MovieCategoryViewHolder(
        val binding: ItemMovieCategoryBinding,
        onMovieItemClickListener: (MovieListItem) -> Unit,
        context: Context
    ) : RecyclerView.ViewHolder(binding.root) {

        val snapHelper = GravitySnapHelper(Gravity.START)

        val adapter = MovieListAdapter(onMovieItemClickListener)

        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
    }
}