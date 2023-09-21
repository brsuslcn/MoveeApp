package com.adesso.moveeapp.ui.home.searchscreen.model

import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import com.adesso.moveeapp.R
import com.adesso.moveeapp.navigation.graphs.DetailScreens
import com.adesso.moveeapp.util.Constants


sealed class MediaTypeUiModel(
    val iconResId: Int,
    val type: String,
    val stringResourceId: Int,
    val getItemName: (String?, String?) -> String?,
    val getItemImage: (String?, String?) -> String?,
    private val navigateAction: (Int, NavController) -> Unit
) {

    fun navigate(itemId: Int, navController: NavController) = navigateAction(itemId, navController)
    object Person : MediaTypeUiModel(
        iconResId = R.drawable.ic_actor,
        type = Constants.PERSON,
        stringResourceId = R.string.search_screen_person,
        getItemName = { name, _ -> name },
        getItemImage = { profilePath, _ -> profilePath ?: ""},
        navigateAction = { itemId, navController ->
            navController.navigate("${DetailScreens.ActorDetailScreen.route}/$itemId")
        }
    )

    object Movie : MediaTypeUiModel(
        iconResId = R.drawable.ic_movies,
        type = Constants.MOVIE,
        stringResourceId = R.string.search_screen_movie,
        getItemName = { _, title -> title },
        getItemImage = { _, posterPath -> posterPath ?: ""},
        navigateAction = { itemId, navController ->
            navController.navigate("${DetailScreens.MovieDetailScreen.route}/$itemId")
        }
    )

    object TvSeries : MediaTypeUiModel(
        iconResId = R.drawable.ic_tv_series,
        type = Constants.TV,
        stringResourceId = R.string.search_screen_tv,
        getItemName = { name, _ -> name },
        getItemImage = { _, posterPath -> posterPath },
        navigateAction = { itemId, navController ->
            navController.navigate("${DetailScreens.TvSeriesDetailScreen.route}/$itemId")
        }
    )
}



