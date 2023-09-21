package com.adesso.moveeapp.ui.home.moviedetailscreen.model

data class MovieDetailsUiModel(
    val id: Int,
    val name: String,
    val duration: String,
    val releaseDate: String,
    val genre: String,
    val rate: Double,
    val imagePath: String,
    val overview: String,
    val directors: String,
    val authors: String,
    val stars: String
)