package com.adesso.moveeapp.ui.home.moviedetailscreen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.adesso.moveeapp.R
import com.adesso.moveeapp.ui.components.CircularProgress
import com.adesso.moveeapp.ui.home.components.FavoriteStatus
import com.adesso.moveeapp.ui.home.components.ItemCover
import com.adesso.moveeapp.ui.home.components.ItemInfo
import com.adesso.moveeapp.ui.theme.SystemUi
import com.adesso.moveeapp.ui.viewmodel.SharedViewModel
import com.adesso.moveeapp.util.Constants
import com.adesso.moveeapp.util.state.DataState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MovieDetailScreen(
    movieId: Int,
    viewModel: MovieDetailScreenViewModel = hiltViewModel(),
    sharedViewModel: SharedViewModel = hiltViewModel()
) {
    val scrollState = rememberScrollState()
    val movieInfo = viewModel.singleMovieInfoFlow.collectAsState().value

    SystemUi.Color(color = Color.Transparent)
    LaunchedEffect(Unit) {
        viewModel.getSingleMovieInfo(movieId)
        SystemUi.fileSystemWindows.value = false
    }

    FavoriteStatus()

    when (movieInfo) {
        is DataState.Initial -> {}
        is DataState.Loading -> {
            CircularProgress()
        }

        is DataState.Success -> {
            val details = movieInfo.data
            Scaffold { paddingValue ->
                Column(
                    modifier = Modifier
                        .verticalScroll(scrollState)
                        .padding(bottom = paddingValue.calculateBottomPadding().value.dp + 10.dp)
                ) {
                    ItemCover(
                        imagePath = details.imagePath,
                        itemRate = details.rate.toString(),
                        addFavorite = { sharedViewModel.changeFavorite(movieId, Constants.MOVIE) }
                    )
                    ItemInfo(
                        modifier = Modifier
                            .padding(start = 32.dp, end = 32.dp, top = 8.dp),
                        itemId = details.id,
                        itemType = Constants.MOVIE,
                        itemName = details.name,
                        itemOverview = details.overview,
                        itemDuration = details.duration,
                        itemGenre = details.genre,
                        itemReleaseDate = details.releaseDate
                    )
                    MovieSummary(
                        modifier = Modifier
                            .padding(start = 32.dp, end = 32.dp, top = 16.dp),
                        overview = details.overview,
                        director = details.directors,
                        author = details.authors,
                        stars = details.stars
                    )
                }
            }
        }

        is DataState.Error -> {
            Text("An error occurred! Please try again!")
        }
    }
}

@Composable
private fun MovieSummary(
    modifier: Modifier = Modifier,
    overview: String,
    director: String,
    author: String,
    stars: String
) {
    val labelDirector = stringResource(id = R.string.movies_detail_screen_director)
    val labelWriter = stringResource(id = R.string.movies_detail_screen_writers)
    val labelStar = stringResource(id = R.string.movies_detail_screen_stars)

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = overview,
            fontSize = 17.sp,
        )
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {

            if (director.isNotEmpty()) {
                MovieSummaryExtension(labelName = labelDirector, name = director)
            }

            if (author.isNotEmpty()) {
                MovieSummaryExtension(labelName = labelWriter, name = author)
            }

            if (stars.isNotEmpty()) {
                MovieSummaryExtension(labelName = labelStar, name = stars)
            }
        }
    }
}

@Composable
private fun MovieSummaryExtension(modifier: Modifier = Modifier, labelName: String, name: String) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(5.dp)
    ) {
        Text(
            text = labelName,
            fontSize = 17.sp
        )
        Text(
            text = name,
            fontSize = 17.sp,
            color = Color(0xff003dff)
        )
    }
}

@Preview
@Composable
fun MoviesDetailScreenPreview() {
    MovieDetailScreen(0)
}