package com.adesso.moveeapp.navigation

import com.adesso.moveeapp.R

sealed class BottomBarScreen(
    val route: String,
    val icon: Int,
    val selectedIcon: Int
) {
    object Movies : BottomBarScreen(
        route = "MOVIES_HOME_SCREEN",
        icon = R.drawable.ic_bottombar_movie,
        selectedIcon = R.drawable.ic_bottombar_movie_selected
    )

    object TvSeries : BottomBarScreen(
        route = "TV_SERIES_SCREEN",
        icon = R.drawable.ic_bottombar_tv_series,
        selectedIcon = R.drawable.ic_bottombar_tv_series_selected
    )

    object Search : BottomBarScreen(
        route = "SEARCH_SCREEN",
        icon = R.drawable.ic_bottombar_search,
        selectedIcon = R.drawable.ic_bottombar_search_selected
    )

    object Profile: BottomBarScreen(
        route = "PROFILE_SCREEN",
        icon = R.drawable.ic_bottombar_profile,
        selectedIcon = R.drawable.ic_bottombar_profile_selected
    )
}