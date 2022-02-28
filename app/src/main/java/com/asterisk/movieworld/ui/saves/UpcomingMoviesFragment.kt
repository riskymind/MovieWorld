package com.asterisk.movieworld.ui.saves

import android.os.Bundle
import android.view.View
import android.widget.AbsListView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.asterisk.movieworld.R
import com.asterisk.movieworld.databinding.FragmentUpcomingMoviesBinding
import com.asterisk.movieworld.others.Constants
import com.asterisk.movieworld.others.Constants.API_KEY
import com.asterisk.movieworld.others.Resource
import com.asterisk.movieworld.shared.MovieWorldAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UpcomingMoviesFragment : Fragment(R.layout.fragment_upcoming_movies) {

    private var _binding: FragmentUpcomingMoviesBinding? = null
    private val binding get() = _binding!!

    private lateinit var mUpcomingAdapter: MovieWorldAdapter
    private val viewModel by viewModels<UpcomingFragmentViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentUpcomingMoviesBinding.bind(view)

        mUpcomingAdapter = MovieWorldAdapter {
            val action =
                UpcomingMoviesFragmentDirections.actionSaveMoviesFragmentToMovieDetailFragment(
                    it.id.toString(), it.originalTitle
                )
            findNavController().navigate(action)
        }

        setUpRecyclerView()

        viewModel.upComing.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Success -> {
                    hideProgressBar()
                    response.data?.let {
                        mUpcomingAdapter.differ.submitList(it.results.toList())
                    }
                }

                is Resource.Error -> {
                    response.message?.let {
                        Toast.makeText(
                            requireContext(),
                            "an error occurred $it",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
                is Resource.Loading -> {
                    showProgressBar()
                }
            }
        }
    }

    var isLoading = false
    var isLastPage = false
    var isScrolling = false

    private val rvScrollListener = object : RecyclerView.OnScrollListener() {
        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            // checking if it scrolling
            if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                isScrolling = true
            }
        }

        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)

            val layoutManager = recyclerView.layoutManager as LinearLayoutManager
            val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
            val visibleItemCount = layoutManager.childCount
            val totalItemCount = layoutManager.itemCount


            val isNotLoadingAndNotLastPage = !isLoading && !isLastPage
            val isAtLastItem = firstVisibleItemPosition + visibleItemCount >= totalItemCount
            val isNotAtBeginning = firstVisibleItemPosition >= 0
            val isTotalMoreThanVisible = totalItemCount >= Constants.QUERY_PAGE_SIZE

            val shouldPaginate = isNotLoadingAndNotLastPage
                    && isAtLastItem
                    && isNotAtBeginning
                    && isTotalMoreThanVisible
                    && isScrolling
            if (shouldPaginate) {
                viewModel.getUpcomingMovie(API_KEY)
                isScrolling = false
            }
        }
    }

    private fun setUpRecyclerView() {
        binding.rvMovies.apply {
            adapter = mUpcomingAdapter
            layoutManager = LinearLayoutManager(requireContext())
            addOnScrollListener(rvScrollListener)
        }
    }

    private fun hideProgressBar() {
        binding.progressBar.visibility = View.GONE
    }

    private fun showProgressBar() {
        binding.progressBar.visibility = View.VISIBLE
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}