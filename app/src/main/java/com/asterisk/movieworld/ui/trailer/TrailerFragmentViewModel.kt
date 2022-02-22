package com.asterisk.movieworld.ui.trailer

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.asterisk.movieworld.data.MovieRepository
import com.asterisk.movieworld.data.services.tdbmmodel.MovieTrailerResponse
import com.asterisk.movieworld.others.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class TrailerFragmentViewModel @Inject constructor(
    private val movieRepository: MovieRepository
) : ViewModel() {

    private val _trailers: MutableLiveData<Resource<MovieTrailerResponse>> = MutableLiveData()
    val trailers: LiveData<Resource<MovieTrailerResponse>> = _trailers

    fun getTrailers(apiKey: String, movieId: String) = viewModelScope.launch {
        _trailers.postValue(Resource.Loading())
        try {
            val response = movieRepository.getMovieTrailer(apiKey = apiKey, movieId = movieId)
            _trailers.postValue(handleResponse(response))
        } catch (e: Exception) {
            throw e
        }
    }

    private fun handleResponse(response: Response<MovieTrailerResponse>): Resource<MovieTrailerResponse> {
        if (response.isSuccessful) {
            response.body()?.let {
                return Resource.Success(it)
            }
        }

        return Resource.Error(response.message())
    }
}