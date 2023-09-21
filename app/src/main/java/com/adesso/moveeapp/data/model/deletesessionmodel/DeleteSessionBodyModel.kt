package com.adesso.moveeapp.data.model.deletesessionmodel

import com.google.gson.annotations.SerializedName

data class DeleteSessionBodyModel(
    @SerializedName("session_id")
    val sessionId : String?
)
