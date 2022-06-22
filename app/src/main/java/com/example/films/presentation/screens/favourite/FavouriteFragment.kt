package com.example.films.presentation.screens.favourite

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.films.appComponent
import com.example.films.databinding.FragmentFavouriteBinding
import com.example.films.presentation.models.MovieListItem
import javax.inject.Inject

class FavouriteFragment : Fragment() {

    private lateinit var binding: FragmentFavouriteBinding

    @Inject
    lateinit var viewModelFactory: FavouriteViewModel.Factory
    private val viewModel: FavouriteViewModel by viewModels { viewModelFactory }

    private lateinit var adapter: FavouriteMovieListAdapter

    override fun onAttach(context: Context) {
        appComponent.inject(this)
        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFavouriteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getSavedFavouriteMovies()

        setupRecyclerView()
        observe()
    }

    private fun setupRecyclerView() {
        adapter = FavouriteMovieListAdapter { movieListItem ->
            openMovieDetails(movieListItem)
        }
        with(binding.savedMovieListRecyclerView) {
            adapter = this@FavouriteFragment.adapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    private fun openMovieDetails(movieListItem: MovieListItem) {
        val action =
            FavouriteFragmentDirections.actionFavouriteFragmentToMovieDetailsFragment(movieListItem)
        findNavController().navigate(action)
    }

    private fun observe() {
        with(viewModel) {
            savedMovieList.observe(viewLifecycleOwner) {
                adapter.currentList = it

                binding.noSavedMoviesTextView.visibility = if (it.isEmpty()) {
                    View.VISIBLE
                } else {
                    View.GONE
                }
            }
        }
    }
}