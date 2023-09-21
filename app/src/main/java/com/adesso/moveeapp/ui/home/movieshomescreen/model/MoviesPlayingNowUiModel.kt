package com.adesso.moveeapp.ui.home.movieshomescreen.model

data class MoviesPlayingNowUiModel(
    val imagePath: String,
    val name: String,
    val genre: String,
    val voteAverage: Double,
    val id: Int
)