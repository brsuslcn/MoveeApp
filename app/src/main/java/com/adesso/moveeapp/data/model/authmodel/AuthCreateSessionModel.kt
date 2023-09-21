package com.adesso.moveeapp.data.model.authmodel

import com.google.gson.annotations.SerializedName

data class AuthCreateSessionModel(
    @SerializedName("session_id")
    val sessionId: String,
    @SerializedName("success")
    val success: Boolean
)