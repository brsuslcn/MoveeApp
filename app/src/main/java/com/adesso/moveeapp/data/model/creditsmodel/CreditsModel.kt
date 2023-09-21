package com.adesso.moveeapp.data.model.creditsmodel

import com.google.gson.annotations.SerializedName

data class CreditsModel(
    @SerializedName("cast")
    val cast: List<Cast>?,
    @SerializedName("crew")
    val crew: List<Crew>?,
    @SerializedName("id")
    val id: Int
)