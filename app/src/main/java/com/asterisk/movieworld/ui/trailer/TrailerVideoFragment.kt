package com.asterisk.movieworld.ui.trailer

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.asterisk.movieworld.R
import com.asterisk.movieworld.databinding.FragmentTrailerVideoBinding
import com.asterisk.movieworld.others.Constants.API_KEY
import com.asterisk.movieworld.others.Resource
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class TrailerVideoFragment : Fragment(R.layout.fragment_trailer_video) {

    private var _binding: FragmentTrailerVideoBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<TrailerFragmentViewModel>()
    private val id by navArgs<TrailerVideoFragmentArgs>()

    private lateinit var trailerAdapter: TrailerAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentTrailerVideoBinding.bind(view)
        trailerAdapter = TrailerAdapter()
        setUpRecyclerview()

        viewModel.getTrailers(apiKey = API_KEY, movieId = id.movieId)

        viewModel.trailers.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Success -> {
                    hideProgressBar()
                    response.data?.let {
                        trailerAdapter.differ.submitList(it.results)
                    }
                }
                is Resource.Error -> {
                    hideProgressBar()
                }
                is Resource.Loading -> {
                    showProgressBar()
                }
            }
        }
    }

    private fun setUpRecyclerview() {
        binding.apply {
            rvTrailer.apply {
                adapter = trailerAdapter
            }
        }
    }

    private fun showProgressBar() {
        binding.progressBar.visibility = View.VISIBLE
    }

    private fun hideProgressBar() {
        binding.progressBar.visibility = View.GONE
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}