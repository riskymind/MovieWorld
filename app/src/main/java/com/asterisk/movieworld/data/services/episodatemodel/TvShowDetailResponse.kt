package com.asterisk.movieworld.data.services.episodatemodel


import com.google.gson.annotations.SerializedName

data class TvShowDetailResponse(
    @SerializedName("tvShow")
    val tvShow: TvShowX
)