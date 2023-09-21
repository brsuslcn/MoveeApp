package com.adesso.moveeapp.ui.home.movieshomescreen.model

data class MoviesPopularUiModel(
    val id: Int,
    val imagePath: String,
    val name: String,
    val genre: String,
    val voteAverage: Double,
    val date: String,
)
