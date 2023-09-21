package com.adesso.moveeapp.ui.home.tvseriesdetailscreen.model

data class TvSeriesDetailsUiModel(
    val id: Int,
    val imagePath: String,
    val rate: Double,
    val name: String,
    val genre: String,
    val duration: String,
    val releaseDate: String,
    val overview: String,
    val creators: String,
    val seasons: String,
)