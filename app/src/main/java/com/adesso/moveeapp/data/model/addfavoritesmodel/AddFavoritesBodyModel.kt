package com.adesso.moveeapp.data.model.addfavoritesmodel

import com.google.gson.annotations.SerializedName

data class AddFavoritesBodyModel(
    @SerializedName("media_type")
    val mediaType: String,
    @SerializedName("media_id")
    val mediaId: Int,
    @SerializedName("favorite")
    val favorite: Boolean
)