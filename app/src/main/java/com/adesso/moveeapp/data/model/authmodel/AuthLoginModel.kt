package com.adesso.moveeapp.data.model.authmodel

import com.google.gson.annotations.SerializedName

data class AuthLoginModel(
    @SerializedName("expires_at")
    val expiresAt: String,
    @SerializedName("request_token")
    val requestToken: String,
    @SerializedName("success")
    val success: Boolean
)