package com.asterisk.movieworld.ui.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.asterisk.movieworld.databinding.DialogSimilarBinding
import com.asterisk.movieworld.others.Constants.API_KEY
import com.asterisk.movieworld.others.Resource
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SimilarDialog : BottomSheetDialogFragment() {

    private var _binding: DialogSimilarBinding? = null
    private val binding get() = _binding!!

    private val movieId by navArgs<SimilarDialogArgs>()
    private val viewModel by viewModels<SimilarDialogViewModel>()
    private lateinit var similarMovieAdapter: SimilarMovieAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = DialogSimilarBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        similarMovieAdapter = SimilarMovieAdapter()
        setupRecyclerView()
        viewModel.getSimilarMovie(API_KEY, movieId.movieId)

        viewModel.similarMovies.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Success -> {
//                    hideProgressBar()
                    response.data?.let {
                        similarMovieAdapter.differ.submitList(it.results)
                    }
                }

                is Resource.Error -> {

                }

                is Resource.Loading -> {
//                    showProgressBar()
                }
            }
        }

    }

    private fun setupRecyclerView() {
        binding.apply {
            rvSimilar.apply {
                adapter = similarMovieAdapter
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}