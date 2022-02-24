package com.asterisk.movieworld.ui.saves

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.asterisk.movieworld.R
import com.asterisk.movieworld.databinding.FragmentUpcomingMoviesBinding
import com.asterisk.movieworld.others.Resource
import com.asterisk.movieworld.shared.NowPlayingAdapter
import com.asterisk.movieworld.ui.now_playing.NowPlayingFragmentDirections
import com.asterisk.movieworld.ui.now_playing.NowPlayingFragmentViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UpcomingMoviesFragment : Fragment(R.layout.fragment_upcoming_movies) {

    private var _binding: FragmentUpcomingMoviesBinding? = null
    private val binding get() = _binding!!

    private lateinit var upcomingAdapter: NowPlayingAdapter
    private val viewModel by viewModels<UpcomingFragmentViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentUpcomingMoviesBinding.bind(view)

        upcomingAdapter = NowPlayingAdapter {
            val action =
                UpcomingMoviesFragmentDirections.
                actionSaveMoviesFragmentToMovieDetailFragment(it.id.toString())
            findNavController().navigate(action)
        }

        setUpRecyclerView()

        viewModel.upComing.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Success -> {
                    hideProgressBar()
                    response.data?.let {
                        upcomingAdapter.differ.submitList(it.results)
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

    private fun setUpRecyclerView() {
        binding.rvMovies.apply {
            adapter = upcomingAdapter
            layoutManager = LinearLayoutManager(requireContext())
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