package com.asterisk.movieworld.data.services.tdbmmodel


import com.google.gson.annotations.SerializedName

data class MovieTrailerResponse(
    @SerializedName("id")
    val id: Int,
    @SerializedName("results")
    val results: List<ResultX>
)