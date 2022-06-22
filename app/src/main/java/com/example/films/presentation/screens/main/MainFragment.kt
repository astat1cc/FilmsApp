package com.example.films.presentation.screens.main

import android.content.Context
import android.net.ConnectivityManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.films.Const.POPULAR_MOVIES_ID
import com.example.films.Const.TOP_RATED_MOVIES_ID
import com.example.films.Const.UPCOMING_MOVIES_ID
import com.example.films.appComponent
import com.example.films.databinding.FragmentMainBinding
import com.example.films.presentation.models.MovieListItem
import com.example.films.presentation.screens.common.UiState
import com.example.films.presentation.screens.main.adapters.MovieCategoriesAdapter
import javax.inject.Inject


class MainFragment : Fragment() {

    private lateinit var binding: FragmentMainBinding

    private lateinit var movieCategoriesAdapter: MovieCategoriesAdapter

    @Inject
    lateinit var viewModelFactory: MainViewModel.Factory

    private val viewModel: MainViewModel by viewModels { viewModelFactory }

    override fun onAttach(context: Context) {
        appComponent.inject(this)
        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getAllMoviesMainPage()

        observe()
        setupRecyclerView()

        binding.errorCaseHolder.tryAgainButton.setOnClickListener {
            getAllMoviesMainPage()
        }
    }

    private fun getAllMoviesMainPage() {
        viewModel.getAllMovies()
    }

    private fun setupRecyclerView() {
        movieCategoriesAdapter = MovieCategoriesAdapter(
            onMovieItemClickListener = { movie ->
                openMovieDetails(movie)
            },
            onSaveScrollPositionListener = { movieCategoryId, position ->
                viewModel.saveScrollPosition(movieCategoryId, position)
            }
        )
        binding.movieCategoryListRecyclerView.apply {
            adapter = movieCategoriesAdapter
            layoutManager = LinearLayoutManager(requireContext())

            addOnItemTouchListener(createInterruptingOuterScrollTouchListener())
        }
    }

    // listener that stops outer (vertical) recycler view scroll when user start scrolling some of the inner (horizontal) recycler views
    private fun createInterruptingOuterScrollTouchListener() =
        object : RecyclerView.OnItemTouchListener {
            override fun onInterceptTouchEvent(rv: RecyclerView, e: MotionEvent): Boolean {
                if (e.action == MotionEvent.ACTION_DOWN &&
                    rv.scrollState == RecyclerView.SCROLL_STATE_SETTLING
                ) {
                    rv.stopScroll()
                }
                return false
            }

            override fun onTouchEvent(rv: RecyclerView, e: MotionEvent) {}
            override fun onRequestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {}
        }

    private fun openMovieDetails(movie: MovieListItem) {
        val action = MainFragmentDirections.actionMainFragmentToMovieDetailsFragment(movie)
        findNavController().navigate(action)
    }

    private fun observe() {
        with(viewModel) {
            popularMovieList.observe(viewLifecycleOwner) {
                updateMovieList(it, POPULAR_MOVIES_ID)
            }

            topRatedMovieList.observe(viewLifecycleOwner) {
                updateMovieList(it, TOP_RATED_MOVIES_ID)
            }

            upcomingMovieList.observe(viewLifecycleOwner) {
                updateMovieList(it, UPCOMING_MOVIES_ID)
            }

            movieCategoryList.observe(viewLifecycleOwner) {
                movieCategoriesAdapter.currentCategoryList = it.map { category ->
                    category.copy() // to avoid mutability problem
                }
            }

            uiState.observe(viewLifecycleOwner) { state ->
                when (state) {
                    is UiState.Loading -> setLoadingUi()
                    is UiState.LoadSuccess -> setSuccessUi()
                    is UiState.LoadError -> setErrorUi()
                }
            }

            scrollPositions.observe(viewLifecycleOwner) {
                movieCategoriesAdapter.innerRecyclerViewSavedScrollPositions = it
            }
        }
    }

    private fun setSuccessUi() {
        with(binding) {
            movieCategoryListRecyclerView.visibility = View.VISIBLE
            progressBar.visibility = View.GONE
            errorCaseHolder.root.visibility = View.GONE
        }
    }

    private fun setLoadingUi() {
        with(binding) {
            movieCategoryListRecyclerView.visibility = View.GONE
            progressBar.visibility = View.VISIBLE
            errorCaseHolder.root.visibility = View.GONE
        }
    }

    private fun setErrorUi() {
        with(binding) {
            movieCategoryListRecyclerView.visibility = View.GONE
            progressBar.visibility = View.GONE
            errorCaseHolder.root.visibility = View.VISIBLE
        }
    }


    companion object {
        const val ADAPTER = "adapter"
    }
}