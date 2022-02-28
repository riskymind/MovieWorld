package com.asterisk.movieworld.ui.now_playing

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.asterisk.movieworld.data.MovieRepository
import com.asterisk.movieworld.data.services.tdbmmodel.MovieResponse
import com.asterisk.movieworld.others.Constants.API_KEY
import com.asterisk.movieworld.others.NetworkUtil
import com.asterisk.movieworld.others.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class NowPlayingFragmentViewModel @Inject constructor(
    private val movieRepository: MovieRepository,
    private val networkUtil: NetworkUtil
) : ViewModel() {

    val nowPlaying: MutableLiveData<Resource<MovieResponse>> = MutableLiveData()
    var page = 1
    var nowPlayingResponse: MovieResponse? = null


    init {
        if (networkUtil.isConnected()) {
            getNowPlaying(API_KEY)
        } else {
            nowPlaying.postValue(Resource.Error(message = "there is no network connection"))
        }

    }


    fun getNowPlaying(apiKey: String) = viewModelScope.launch {
        nowPlaying.postValue(Resource.Loading())
        try {
            val response = movieRepository.getNowPlaying(apiKey, page)
            nowPlaying.postValue(handleNowPlayingResponse(response))
        } catch (e: Exception) {
            throw e
        }
    }

    private fun handleNowPlayingResponse(response: Response<MovieResponse>): Resource<MovieResponse> {
        if (response.isSuccessful) {
            response.body()?.let {
                page++
                if (nowPlayingResponse == null) {
                    nowPlayingResponse = it
                } else {
                    val oldMovies = nowPlayingResponse?.results
                    val newMovies = it.results
                    oldMovies?.addAll(newMovies)
                }
                return Resource.Success(nowPlayingResponse ?: it)
            }
        }

        return Resource.Error(response.message())
    }
}