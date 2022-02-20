package com.asterisk.movieworld.data

import com.asterisk.movieworld.data.services.MovieApi
import javax.inject.Inject

class MovieRepository @Inject constructor(
    private val movieApi: MovieApi
) {

    suspend fun getNowPlaying(apiKey: String, page: Int) =
        movieApi.getNowPlayingMovies(apiKey, page)

    suspend fun getMovieDetail(apiKey: String, movieId: String) =
        movieApi.getMovieDetail(movieId = movieId, apiKey = apiKey)

    suspend fun getMovieImages(apiKey: String, movieId: String) =
        movieApi.getMovieImages(movieId, apiKey)
}