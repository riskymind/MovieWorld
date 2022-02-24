package com.asterisk.movieworld.data

import com.asterisk.movieworld.data.services.MovieApi
import javax.inject.Inject

class MovieRepository @Inject constructor(
    private val movieApi: MovieApi
) {

    suspend fun getNowPlaying(apiKey: String, page: Int) =
        movieApi.getNowPlayingMovies(apiKey = apiKey, page = page)

    suspend fun getMovieDetail(apiKey: String, movieId: String) =
        movieApi.getMovieDetail(movieId = movieId, apiKey = apiKey)

    suspend fun getMovieImages(apiKey: String, movieId: String) =
        movieApi.getMovieImages(movieId = movieId, apiKey = apiKey)

    suspend fun getMovieTrailer(apiKey: String, movieId: String) =
        movieApi.getMovieTrailer(movieId = movieId, apiKey = apiKey)

    suspend fun getSimilarMovies(apiKey: String, movieId: String) =
        movieApi.getSimilarMovie(movieId = movieId, apiKey = apiKey)

    suspend fun getMoviesCredit(apiKey: String, movieId: String) =
        movieApi.getMovieCredit(movieId = movieId, apiKey = apiKey)

    suspend fun getUpcomingMovies(apiKey: String, page: Int) =
        movieApi.getUpcomingMovies(apiKey = apiKey, page = page)
}