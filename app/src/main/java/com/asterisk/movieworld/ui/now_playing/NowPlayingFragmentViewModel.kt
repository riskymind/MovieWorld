package com.asterisk.movieworld.ui.now_playing

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.asterisk.movieworld.data.MovieRepository
import com.asterisk.movieworld.data.services.tdbmmodel.MovieResponse
import com.asterisk.movieworld.others.Constants.API_KEY
import com.asterisk.movieworld.others.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class NowPlayingFragmentViewModel @Inject constructor(
    private val movieRepository: MovieRepository
) : ViewModel() {

    val nowPlaying: MutableLiveData<Resource<MovieResponse>> = MutableLiveData()
    var page = 1


    init {
        getNowPlaying(API_KEY)
    }


    private fun getNowPlaying(apiKey: String) = viewModelScope.launch {
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
                return Resource.Success(it)
            }
        }

        return Resource.Error(response.message())
    }
}