package com.adesso.moveeapp.data.model.authmodel

import com.google.gson.annotations.SerializedName

data class AuthUserModel(
    @SerializedName("username")
    val username: String,
    @SerializedName("password")
    val password: String
)
