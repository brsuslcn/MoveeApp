package com.adesso.moveeapp.navigation.graphs

import androidx.compose.runtime.Composable
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.adesso.moveeapp.navigation.BottomBarScreen
import com.adesso.moveeapp.ui.home.actordetailscreen.ActorDetailScreen
import com.adesso.moveeapp.ui.home.moviedetailscreen.MovieDetailScreen
import com.adesso.moveeapp.ui.home.movieshomescreen.MoviesHomeScreen
import com.adesso.moveeapp.ui.home.profilescreen.ProfileScreen
import com.adesso.moveeapp.ui.home.searchscreen.SearchScreen
import com.adesso.moveeapp.ui.home.tvseriesdetailscreen.TvSeriesDetailScreen
import com.adesso.moveeapp.ui.home.tvserieshomescreen.TvSeriesScreen

@Composable
fun HomeNavGraph(
    navController: NavHostController,
) {
    NavHost(
        navController = navController,
        route = Graph.HOME,
        startDestination = BottomBarScreen.Movies.route
    ) {

        composable(route = BottomBarScreen.Movies.route) {
            MoviesHomeScreen(
                navController = navController
            )
        }

        composable(route = BottomBarScreen.TvSeries.route) {
            TvSeriesScreen(
                navController = navController
            )
        }

        composable(route = BottomBarScreen.Search.route) {
            SearchScreen(
                navController = navController
            )
        }

        composable(route = BottomBarScreen.Profile.route) {
            ProfileScreen(
                navController = navController
            )
        }

        detailNavGraph(navController = navController)

    }
}

private fun NavGraphBuilder.detailNavGraph(navController: NavHostController) {
    navigation(
        route = Graph.DETAIL,
        startDestination = DetailScreens.MovieDetailScreen.route
    ) {
        composable(route = "${DetailScreens.TvSeriesDetailScreen.route}/{tv_series_id}") { backStackEntry ->
            val tvSeriesId = backStackEntry.arguments?.getString("tv_series_id")
            TvSeriesDetailScreen(
                navController = navController,
                tvSeriesId = tvSeriesId!!.toInt()
            )
        }

        composable(route = "${DetailScreens.ActorDetailScreen.route}/{actor_id}") { backStackEntry ->
            val actorId = backStackEntry.arguments?.getString("actor_id")
            ActorDetailScreen(
                navController = navController,
                actorId = actorId!!.toInt()
            )
        }

        composable(route = "${DetailScreens.MovieDetailScreen.route}/{movie_id}") { backStackEntry ->
            val movieId = backStackEntry.arguments?.getString("movie_id")
            MovieDetailScreen(movieId = movieId!!.toInt())
        }
    }
}

sealed class DetailScreens(val route: String) {
    object MovieDetailScreen : DetailScreens(route = "MOVIE_DETAIL")
    object TvSeriesDetailScreen : DetailScreens(route = "TV_DETAIL")
    object ActorDetailScreen : DetailScreens(route = "ACTOR_DETAIL")
}