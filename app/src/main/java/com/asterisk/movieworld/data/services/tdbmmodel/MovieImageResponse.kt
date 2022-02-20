package com.asterisk.movieworld.data.services.tdbmmodel


import com.google.gson.annotations.SerializedName

data class MovieImageResponse(
    @SerializedName("backdrops")
    val backdrops: List<Backdrop>,
    @SerializedName("id")
    val id: Int,
    @SerializedName("logos")
    val logos: List<Logo>,
    @SerializedName("posters")
    val posters: List<Poster>
)