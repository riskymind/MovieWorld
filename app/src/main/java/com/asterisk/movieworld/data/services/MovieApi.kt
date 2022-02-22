package com.asterisk.movieworld.data.services

import com.asterisk.movieworld.data.services.tdbmmodel.MovieDetailResponse
import com.asterisk.movieworld.data.services.tdbmmodel.MovieImageResponse
import com.asterisk.movieworld.data.services.tdbmmodel.MovieResponse
import com.asterisk.movieworld.data.services.tdbmmodel.MovieTrailerResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MovieApi {

    @GET("movie/now_playing")
    suspend fun getNowPlayingMovies(
        @Query("api_key") apiKey: String,
        @Query("page") page: Int = 1
    ): Response<MovieResponse>

    @GET("movie/{movie_id}")
    suspend fun getMovieDetail(
        @Path("movie_id") movieId: String,
        @Query("api_key") apiKey: String
    ): Response<MovieDetailResponse>

    @GET("movie/{movie_id}/images")
    suspend fun getMovieImages(
        @Path("movie_id") movieId: String,
        @Query("api_key") apiKey: String
    ): Response<MovieImageResponse>

    @GET("movie/{movie_id}/videos")
    suspend fun getMovieTrailer(
        @Path("movie_id") movieId: String,
        @Query("api_key") apiKey: String
    ): Response<MovieTrailerResponse>

}