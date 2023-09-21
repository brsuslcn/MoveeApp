package com.adesso.moveeapp.data.model.actortvcreditsmodel

import com.google.gson.annotations.SerializedName

data class ActorTvCreditsModel(
    @SerializedName("cast")
    val castTv: List<CastTv>,
    @SerializedName("id")
    val id: Int
)