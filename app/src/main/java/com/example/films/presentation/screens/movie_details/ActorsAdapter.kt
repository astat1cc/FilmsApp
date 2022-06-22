package com.example.films.presentation.screens.movie_details

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.films.databinding.ItemActorBinding
import com.example.films.presentation.models.ItemActor

class ActorsAdapter : RecyclerView.Adapter<ActorsAdapter.ActorViewHolder>() {

    class ActorViewHolder(val binding: ItemActorBinding) : RecyclerView.ViewHolder(binding.root)

    var currentList: List<ItemActor>
        get() = differ.currentList
        set(value) = differ.submitList(value)

    private val diffCallback = object : DiffUtil.ItemCallback<ItemActor>() {
        override fun areItemsTheSame(oldItem: ItemActor, newItem: ItemActor): Boolean =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: ItemActor, newItem: ItemActor): Boolean =
            oldItem == newItem
    }

    private val differ = AsyncListDiffer(this, diffCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ActorViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemActorBinding.inflate(inflater, parent, false)
        return ActorViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ActorViewHolder, position: Int) {
        val actor = differ.currentList[position]
        with(holder.binding) {
            Glide.with(root).load("$PROFILE_IMAGES_BASE_URL${actor.profile_path}").into(actorImage)

            val splitName = actor.name.split(" ")
            actorFirstNameTextView.text = splitName.first()
            actorLastNameTextView.text =
                if (splitName.size > 1) {
                    splitName.drop(1).joinToString(" ")
                } else {
                    ""
                }
        }
    }

    override fun getItemCount(): Int = differ.currentList.size

    companion object {
        const val PROFILE_IMAGES_BASE_URL = "https://image.tmdb.org/t/p/w185"
    }
}