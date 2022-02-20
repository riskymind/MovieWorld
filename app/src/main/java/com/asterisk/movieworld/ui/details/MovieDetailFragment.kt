package com.asterisk.movieworld.ui.details

import android.content.Intent
import android.graphics.Color
import android.icu.text.CaseMap
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.text.HtmlCompat
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.asterisk.movieworld.R
import com.asterisk.movieworld.data.services.tdbmmodel.MovieDetailResponse
import com.asterisk.movieworld.databinding.FragmentMovieDetailBinding
import com.asterisk.movieworld.others.Constants.API_KEY
import com.asterisk.movieworld.others.Resource
import com.bumptech.glide.Glide
import com.devs.readmoreoption.ReadMoreOption
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.qualifiers.ApplicationContext
import java.util.*

@AndroidEntryPoint
class MovieDetailFragment : Fragment(R.layout.fragment_movie_detail) {

    private var _binding: FragmentMovieDetailBinding? = null
    private val binding get() = _binding!!

    private lateinit var posterAdapter: PosterAdapter

    private val viewModel by viewModels<MovieDetailViewModel>()

    private val id by navArgs<MovieDetailFragmentArgs>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentMovieDetailBinding.bind(view)


        viewModel.getMovieDetail(apiKey = API_KEY, movieId = id.movieId)
        viewModel.getMovieImages(apiKey = API_KEY, movieId = id.movieId)

        viewModel.movieImages.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Success -> {
                    hidePosterProgressBar()
                    response.data?.let {
                        posterAdapter = PosterAdapter(it.posters)
                        binding.sliderViewPager.adapter = posterAdapter
                        binding.layoutSliderIndicator.setViewPager(binding.sliderViewPager)
                    }
                }

                is Resource.Error -> {

                }

                is Resource.Loading -> {
                    showPosterProgressBar()
                }
            }
        }

        viewModel.details.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Success -> {
                    hideProgressBar()
                    response.data?.let {
                        displayMovieDetail(it)
                    }
                }

                is Resource.Error -> {

                }

                is Resource.Loading -> {
                    showProgressBar()
                }
            }
        }
    }

    private fun displayMovieDetail(movie: MovieDetailResponse) {
        binding.apply {
            Glide.with(requireContext())
                .load("https://image.tmdb.org/t/p/w342/${movie.posterPath}")
                .into(ivPoster)

            tvOriginalTitle.text = movie.originalTitle
            tvAdult.text = movie.adult.toString()
            tvReleaseDate.text = movie.releaseDate
            tvVoteCount.text = movie.voteCount.toString()

            tvOverview.text = movie.overview
            val readMore = ReadMoreOption.Builder(requireContext())
                .textLength(3, ReadMoreOption.TYPE_LINE)
                .moreLabel(getString(R.string.read_more))
                .lessLabel(getString(R.string.read_less))
                .moreLabelColor(Color.RED)
                .lessLabelColor(activity!!.getColor(R.color.colorTextOther))
                .labelUnderLine(true)
                .expandAnimation(true)
                .build()
            readMore.addReadMoreTo(tvOverview, movie.overview)

            tvRating.text = String.format(Locale.getDefault(), "%.2f", movie.voteAverage)
            tvStatus.text = movie.status

            btnWebsite.setOnClickListener {
                val intent = Intent(Intent.ACTION_VIEW)
                intent.data = Uri.parse(movie.homepage)
                startActivity(intent)
            }
        }
    }

    private fun hideProgressBar() {
        binding.progressBar.visibility = View.GONE
    }

    private fun hidePosterProgressBar() {
        binding.posterProgressBar.visibility = View.GONE
    }

    private fun showPosterProgressBar() {
        binding.posterProgressBar.visibility = View.VISIBLE
    }

    private fun showProgressBar() {
        binding.progressBar.visibility = View.VISIBLE
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}