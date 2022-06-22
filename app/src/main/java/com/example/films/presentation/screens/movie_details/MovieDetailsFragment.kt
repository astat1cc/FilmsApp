package com.example.films.presentation.screens.movie_details

import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.example.films.Const.BACKDROP_FILE_SIZE
import com.example.films.Const.BASE_IMAGES_URL
import com.example.films.R
import com.example.films.appComponent
import com.example.films.databinding.FragmentMovieDetailsBinding
import com.example.films.presentation.models.MovieWithDetails
import com.example.films.presentation.screens.common.UiState
import javax.inject.Inject

class MovieDetailsFragment : Fragment() {

    private lateinit var binding: FragmentMovieDetailsBinding
    private val args: MovieDetailsFragmentArgs by navArgs()

    @Inject
    lateinit var viewModelFactory: MovieDetailsViewModel.Factory

    private lateinit var adapter: ActorsAdapter

    private val viewModel: MovieDetailsViewModel by viewModels { viewModelFactory }

    private val movieId: Int
        get() = args.movie.id

    private var isMovieInFavourites = false

    override fun onAttach(context: Context) {
        appComponent.inject(this)
        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMovieDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getMovieDetailsPage()

        setupRecyclerView()
        observe()

        binding.favouriteButton.setOnClickListener {
            onFavouriteButtonPressed()
        }
        binding.errorCaseHolder.tryAgainButton.setOnClickListener {
            getMovieDetailsPage()
        }
    }

    private fun getMovieDetailsPage() {
        with(viewModel) {
            getMovieDetails(movieId)
            checkIfMovieInFavourites(movieId)
        }
    }

    private fun onFavouriteButtonPressed() {
        if (isMovieInFavourites) {
            viewModel.deleteMovie()
        } else {
            viewModel.saveMovie()
        }
    }

    private fun setupRecyclerView() {
        adapter = ActorsAdapter()
        with(binding.castRecyclerView) {
            adapter = this@MovieDetailsFragment.adapter
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        }
    }

    private fun observe() {
        with(viewModel) {
            movie.observe(viewLifecycleOwner) {
                updateUiComponents(it)
            }

            uiState.observe(viewLifecycleOwner) { state ->
                when (state) {
                    is UiState.Loading -> setLoadingUi()
                    is UiState.LoadSuccess -> setSuccessUi()
                    is UiState.LoadError -> setErrorUi()
                }
            }

            actors.observe(viewLifecycleOwner) {
                adapter.currentList = it
            }

            isMovieInFavourites.observe(viewLifecycleOwner) {
                this@MovieDetailsFragment.isMovieInFavourites = it

                binding.favouriteButton.text =
                    if (it) "Delete from Favourites" else "Add to Favourites"
            }
        }
    }

    private fun setLoadingUi() {
        with(binding) {
            binding.wholeMovieHolder.visibility = View.VISIBLE
            binding.posterImageView.visibility = View.INVISIBLE
            progressBar.visibility = View.VISIBLE
            binding.errorCaseHolder.root.visibility = View.GONE
        }
    }

    private fun setSuccessUi() {
        with(binding) {
            binding.wholeMovieHolder.visibility = View.VISIBLE
            binding.posterImageView.visibility = View.VISIBLE
            progressBar.visibility = View.GONE
            binding.errorCaseHolder.root.visibility = View.GONE
        }
    }

    private fun setErrorUi() {
        with(binding) {
            binding.wholeMovieHolder.visibility = View.GONE
            progressBar.visibility = View.GONE
            binding.errorCaseHolder.root.visibility = View.VISIBLE
        }
    }

    private fun updateUiComponents(movie: MovieWithDetails) {
        with(binding) {
            movie.backdrop_path?.let { loadMoviePoster(it) }
            titleTextView.text = movie.title

            with(favouriteButton) {
                visibility = View.VISIBLE
            }

            descriptionTextView.text = movie.overview
            firstRowOfDetailInfoTextView.text = getFirstRowText(movie)
            secondRowOfDetailInfoTextView.text = getSecondRowText(movie)
            setMovieRateText(movie.vote_average)

        }
    }

    private fun setMovieRateText(rate: Double?) {
        rate?.let {
            val textColor = when (rate) {
                in 7.0..10.0 -> ContextCompat.getColor(requireContext(), R.color.highRateTextColor)
                in 5.0..7.0 -> ContextCompat.getColor(requireContext(), R.color.mediumRateTextColor)
                else -> ContextCompat.getColor(requireContext(), R.color.lowRateTextColor)
            }
            with(binding.movieRateTextView) {
                text = rate.toString()
                setTextColor(textColor)
            }
        }
    }

    private fun loadMoviePoster(posterPath: String) {
        Glide.with(requireContext())
            .load("$BASE_IMAGES_URL$BACKDROP_FILE_SIZE${posterPath}")
            .listener(createGlideRequestListener())
            .into(binding.posterImageView)
    }

    private fun createGlideRequestListener(): RequestListener<Drawable> =
        object : RequestListener<Drawable> {
            override fun onLoadFailed(
                e: GlideException?,
                model: Any?,
                target: Target<Drawable>?,
                isFirstResource: Boolean
            ): Boolean {
                Log.d("glide", "${e?.message}")
                return false
            }

            override fun onResourceReady(
                resource: Drawable?,
                model: Any?,
                target: Target<Drawable>?,
                dataSource: DataSource?,
                isFirstResource: Boolean
            ): Boolean {
                viewModel.posterLoaded()
                return false
            }
        }

    private fun getSecondRowText(movie: MovieWithDetails): String =
        movie.genres?.joinToString(", ") { it } ?: ""

    private fun minutesToRuntimeFormat(runtime: Int): String {
        val hours = runtime / 60
        val minutes = runtime % 60
        return "$hours:" +
                (if (minutes < 10) "0" else "") +
                "$minutes"
    }

    private fun getFirstRowText(movie: MovieWithDetails): String {
        val year = movie.release_date?.take(4)
        val runtime = movie.runtime?.let { minutesToRuntimeFormat(it) }
        return "$year, $runtime"
    }
}