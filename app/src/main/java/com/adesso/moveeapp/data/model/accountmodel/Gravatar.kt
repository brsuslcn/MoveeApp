package com.adesso.moveeapp.data.model.accountmodel

import com.google.gson.annotations.SerializedName

data class Gravatar(
    @SerializedName("hash")
    val hash: String
)