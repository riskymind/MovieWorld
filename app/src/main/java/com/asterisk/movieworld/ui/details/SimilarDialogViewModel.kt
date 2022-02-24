package com.asterisk.movieworld.ui.details

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.asterisk.movieworld.data.MovieRepository
import com.asterisk.movieworld.data.services.MovieApi
import com.asterisk.movieworld.data.services.tdbmmodel.SimilarMovieResponse
import com.asterisk.movieworld.others.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class SimilarDialogViewModel @Inject constructor(
    private val movieRepository: MovieRepository
) : ViewModel() {

    private val _similarMovies: MutableLiveData<Resource<SimilarMovieResponse>> = MutableLiveData()
    val similarMovies: LiveData<Resource<SimilarMovieResponse>> = _similarMovies

    fun getSimilarMovie(apiKey: String, movieId: String) = viewModelScope.launch {
        _similarMovies.postValue(Resource.Loading())
        try {
            val response = movieRepository.getSimilarMovies(apiKey = apiKey, movieId = movieId)
            _similarMovies.postValue(handleSimilarMoviesResponse(response))
        } catch (e: Exception) {
            throw e
        }
    }

    private fun handleSimilarMoviesResponse(response: Response<SimilarMovieResponse>): Resource<SimilarMovieResponse> {
        if (response.isSuccessful) {
            response.body()?.let {
                return Resource.Success(it)
            }
        }

        return Resource.Error(response.message())
    }
}