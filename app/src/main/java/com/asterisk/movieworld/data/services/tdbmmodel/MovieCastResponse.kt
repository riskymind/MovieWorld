package com.asterisk.movieworld.data.services.tdbmmodel


import com.google.gson.annotations.SerializedName

data class MovieCastResponse(
    @SerializedName("cast")
    val cast: List<Cast>,
    @SerializedName("crew")
    val crew: List<Any>,
    @SerializedName("id")
    val id: Int
)