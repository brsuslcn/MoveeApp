package com.adesso.moveeapp.navigation.graphs

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.adesso.moveeapp.ui.home.HomeScreen

@Composable
fun RootNavGraph(
    navController: NavHostController,
    isSessionValid: Boolean
) {
    NavHost(
        navController = navController,
        route = Graph.ROOT,
        startDestination = if(isSessionValid) Graph.HOME else Graph.AUTHENTICATION
    ) {

        authNavGraph(
            navController = navController,
        )

        composable(route = Graph.HOME) {
            HomeScreen()
        }
    }

}

object Graph {
    const val ROOT = "root_graph"
    const val AUTHENTICATION = "auth_graph"
    const val HOME = "home_graph"
    const val DETAIL = "details_graph"
}
