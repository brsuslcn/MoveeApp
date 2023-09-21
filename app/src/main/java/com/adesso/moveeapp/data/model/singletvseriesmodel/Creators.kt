package com.adesso.moveeapp.data.model.singletvseriesmodel

import com.google.gson.annotations.SerializedName

data class Creators(
    @SerializedName("id")
    val id : String,
    @SerializedName("credit_id")
    val creditId: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("gender")
    val gender: String,
    @SerializedName("profile_path")
    val profilePath: String
)
