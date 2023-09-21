package com.adesso.moveeapp.data.model.addfavoritesmodel

import com.google.gson.annotations.SerializedName

data class AddFavoritesModel(
    @SerializedName("status_code")
    val statusCode: Int,
    @SerializedName("status_message")
    val statusMessage: String,
    @SerializedName("success")
    val success: Boolean
)