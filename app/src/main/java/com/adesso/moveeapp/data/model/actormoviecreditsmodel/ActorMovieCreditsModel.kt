package com.adesso.moveeapp.data.model.actormoviecreditsmodel

import com.google.gson.annotations.SerializedName

data class ActorMovieCreditsModel(
    @SerializedName("cast")
    val castMovie: List<CastMovie>,
    @SerializedName("id")
    val id: Int
)