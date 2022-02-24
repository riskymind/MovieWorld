package com.asterisk.movieworld.data.services.tdbmmodel


import com.google.gson.annotations.SerializedName

data class UpcomingMovieResponse(
    @SerializedName("dates")
    val dates: DatesX,
    @SerializedName("page")
    val page: Int,
    @SerializedName("results")
    val results: MutableList<Result>,
    @SerializedName("total_pages")
    val totalPages: Int,
    @SerializedName("total_results")
    val totalResults: Int
)