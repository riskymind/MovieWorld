package com.asterisk.movieworld.ui.saves

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.asterisk.movieworld.data.MovieRepository
import com.asterisk.movieworld.data.services.tdbmmodel.UpcomingMovieResponse
import com.asterisk.movieworld.others.Constants.API_KEY
import com.asterisk.movieworld.others.NetworkUtil
import com.asterisk.movieworld.others.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class UpcomingFragmentViewModel @Inject constructor(
    private val movieRepository: MovieRepository,
    private val networkUtil: NetworkUtil
) : ViewModel() {

    val upComing: MutableLiveData<Resource<UpcomingMovieResponse>> = MutableLiveData()
    var page = 1
    var upComingResponse: UpcomingMovieResponse? = null

    init {
        if (networkUtil.isConnected()) {
            getUpcomingMovie(API_KEY)
        } else {
            upComing.postValue(Resource.Error("there is no network connection"))
        }

    }


    fun getUpcomingMovie(apiKey: String) = viewModelScope.launch {
        upComing.postValue(Resource.Loading())
        try {
            val response = movieRepository.getUpcomingMovies(apiKey, page)
            upComing.postValue(handleUpcomingResponse(response))
        } catch (e: Exception) {
            throw e
        }
    }

    private fun handleUpcomingResponse(response: Response<UpcomingMovieResponse>): Resource<UpcomingMovieResponse> {
        if (response.isSuccessful) {
            response.body()?.let {
                page++
                if (upComingResponse == null) {
                    upComingResponse = it
                } else {
                    val oldUpcoming = upComingResponse?.results
                    val newUpcoming = it.results
                    oldUpcoming?.addAll(newUpcoming)
                }
                return Resource.Success(upComingResponse ?: it)
            }
        }

        return Resource.Error(response.message())
    }
}