package com.adesso.moveeapp.data.model.accountstatemodel

import com.google.gson.annotations.SerializedName

data class AccountStateModel(
    @SerializedName("favorite")
    val favorite: Boolean?,
    @SerializedName("id")
    val id: Int,
    @SerializedName("rated")
    val rated: Any?,
    @SerializedName("watch_list")
    val watchList: Any?
)