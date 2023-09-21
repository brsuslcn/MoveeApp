package com.adesso.moveeapp.data.model.authmodel

import com.google.gson.annotations.SerializedName

data class AuthSessionRequestModel(
    @SerializedName("request_token")
    val requestToken: String
)