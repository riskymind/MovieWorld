package com.asterisk.movieworld.ui.now_playing

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.asterisk.movieworld.R
import com.asterisk.movieworld.databinding.FragmentNowPlayingBinding
import com.asterisk.movieworld.others.Resource
import com.asterisk.movieworld.shared.NowPlayingAdapter
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class NowPlayingFragment : Fragment(R.layout.fragment_now_playing) {

    private lateinit var movieAdapter: NowPlayingAdapter

    private var _binding: FragmentNowPlayingBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<NowPlayingFragmentViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = FragmentNowPlayingBinding.bind(view)

        movieAdapter = NowPlayingAdapter {
            val action =
                NowPlayingFragmentDirections.actionNowPlayingFragmentToMovieDetailFragment(it.id.toString())
            findNavController().navigate(action)
        }

        setUpRecyclerView()

        viewModel.nowPlaying.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Success -> {
                    hideProgressBar()
                    response.data?.let {
                        movieAdapter.differ.submitList(it.results)
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

    val rvScrollListener = object : RecyclerView.OnScrollListener() {
        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            // checking if it scrolling
            if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                isScrolling = true
            }
        }

        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
        }
    }

    private fun setUpRecyclerView() {
        binding.rvMovies.apply {
            adapter = movieAdapter
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