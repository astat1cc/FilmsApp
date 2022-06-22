package com.example.films.presentation.screens.main.adapters

import androidx.recyclerview.widget.DiffUtil
import com.example.films.presentation.screens.main.models.MovieCategory

class MovieCategoryDiffCallback(
    val oldList: List<MovieCategory>,
    val newList: List<MovieCategory>
) : DiffUtil.Callback() {

    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
        oldList[oldItemPosition].id == newList[newItemPosition].id

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
        oldList[oldItemPosition] == newList[newItemPosition]
}