package com.asterisk.movieworld.ui.details

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.asterisk.movieworld.data.MovieRepository
import com.asterisk.movieworld.data.services.tdbmmodel.MovieCastResponse
import com.asterisk.movieworld.data.services.tdbmmodel.MovieDetailResponse
import com.asterisk.movieworld.data.services.tdbmmodel.MovieImageResponse
import com.asterisk.movieworld.data.services.tdbmmodel.SimilarMovieResponse
import com.asterisk.movieworld.others.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class MovieDetailViewModel @Inject constructor(
    private val movieRepository: MovieRepository
) : ViewModel() {

    private val _details: MutableLiveData<Resource<MovieDetailResponse>> = MutableLiveData()
    val details: LiveData<Resource<MovieDetailResponse>> = _details

    private val _movieImages: MutableLiveData<Resource<MovieImageResponse>> = MutableLiveData()
    val movieImages: LiveData<Resource<MovieImageResponse>> = _movieImages


    private val _movieCast: MutableLiveData<Resource<MovieCastResponse>> = MutableLiveData()
    val movieCast: LiveData<Resource<MovieCastResponse>> = _movieCast

    fun getMovieDetail(apiKey: String, movieId: String) = viewModelScope.launch {
        _details.postValue(Resource.Loading())

        try {
            val response = movieRepository.getMovieDetail(apiKey = apiKey, movieId = movieId)
            _details.postValue(handleDetailResponse(response))
        } catch (e: Exception) {
            throw e
        }
    }

    fun getMovieImages(apiKey: String, movieId: String) = viewModelScope.launch {
        _movieImages.postValue(Resource.Loading())
        try {
            val response = movieRepository.getMovieImages(apiKey = apiKey, movieId = movieId)
            _movieImages.postValue(handleMovieImageResponse(response))
        } catch (e: Exception) {
            throw e
        }
    }


    fun getMovieCredit(apiKey: String, movieId: String) = viewModelScope.launch {
        _movieCast.postValue(Resource.Loading())
        try {
            val response = movieRepository.getMoviesCredit(apiKey = apiKey, movieId = movieId)
            _movieCast.postValue(handleMoviesCastResponse(response))
        } catch (e: Exception) {
            throw e
        }
    }

    private fun handleDetailResponse(response: Response<MovieDetailResponse>): Resource<MovieDetailResponse> {
        if (response.isSuccessful) {
            response.body()?.let {
                return Resource.Success(it)
            }
        }

        return Resource.Error(response.message())
    }

    private fun handleMovieImageResponse(response: Response<MovieImageResponse>): Resource<MovieImageResponse> {
        if (response.isSuccessful) {
            response.body()?.let {
                return Resource.Success(it)
            }
        }

        return Resource.Error(response.message())
    }

    private fun handleMoviesCastResponse(response: Response<MovieCastResponse>): Resource<MovieCastResponse> {
        if (response.isSuccessful) {
            response.body()?.let {
                return Resource.Success(it)
            }
        }

        return Resource.Error(response.message())
    }


}