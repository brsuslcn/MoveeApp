package com.adesso.moveeapp.data.model.genresmodel

import com.google.gson.annotations.SerializedName

data class GenresModel(
    @SerializedName("genres")
    val genres: List<Genre>
)