package com.adesso.moveeapp.ui.home.tvseriesdetailscreen


import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.adesso.moveeapp.R
import com.adesso.moveeapp.navigation.graphs.DetailScreens
import com.adesso.moveeapp.ui.viewmodel.SharedViewModel
import com.adesso.moveeapp.ui.components.CircularProgress
import com.adesso.moveeapp.ui.home.components.FavoriteStatus
import com.adesso.moveeapp.ui.home.components.ItemCover
import com.adesso.moveeapp.ui.home.components.ItemInfo
import com.adesso.moveeapp.ui.home.tvseriesdetailscreen.model.CastUiModel
import com.adesso.moveeapp.ui.theme.SystemUi
import com.adesso.moveeapp.util.Constants
import com.adesso.moveeapp.util.state.DataState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TvSeriesDetailScreen(
    navController: NavController,
    tvSeriesId: Int,
    viewModel: TvSeriesDetailScreenViewModel = hiltViewModel(),
    sharedViewModel: SharedViewModel = hiltViewModel()
) {
    val scrollState = rememberScrollState()
    val tvSeriesInfo = viewModel.tvSeriesInfoFlow.collectAsState().value
    val casts = viewModel.castFlow.collectAsState().value

    SystemUi.Color(color = Color.Transparent)
    LaunchedEffect(Unit) {
        SystemUi.fileSystemWindows.value = false
        viewModel.getTvSeriesInfo(tvSeriesId)
        viewModel.getCast(tvSeriesId)
    }

    FavoriteStatus()

    when (tvSeriesInfo) {
        is DataState.Initial -> {}
        is DataState.Loading -> {
            CircularProgress()
        }

        is DataState.Success -> {
            val details = tvSeriesInfo.data
            Scaffold { paddingValues ->
                Column(
                    modifier = Modifier
                        .padding(bottom = paddingValues.calculateBottomPadding().value.dp + 10.dp)
                        .verticalScroll(scrollState),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    ItemCover(
                        imagePath = details.imagePath,
                        itemRate = details.rate.toString(),
                        addFavorite = { sharedViewModel.changeFavorite(tvSeriesId, Constants.TV) }
                    )
                    ItemInfo(
                        modifier = Modifier
                            .padding(start = 32.dp, end = 32.dp),
                        itemId = details.id,
                        itemType = Constants.TV,
                        itemName = details.name,
                        itemOverview = details.overview,
                        itemGenre = details.genre,
                        itemDuration = details.duration,
                        itemReleaseDate = details.releaseDate
                    )
                    TvSeriesSummery(
                        modifier = Modifier
                            .padding(start = 32.dp, end = 32.dp),
                        overview = details.overview,
                        creators = details.creators,
                        seasons = details.seasons
                    )
                    CastList(
                        modifier = Modifier
                            .padding(start = 32.dp),
                        casts = casts,
                        navController = navController
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
private fun TvSeriesSummery(
    modifier: Modifier = Modifier,
    overview: String,
    creators: String,
    seasons: String
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = overview,
            fontSize = 17.sp
        )
        TvSeriesSeason(
            seasons = seasons
        )

        if (creators.isNotEmpty()) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(5.dp)
            ) {
                Text(
                    text = stringResource(id = R.string.tvseries_detail_screen_creators),
                    fontSize = 17.sp
                )
                Text(
                    text = creators,
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Blue
                )
            }
        }
    }
}

@Composable
private fun TvSeriesSeason(modifier: Modifier = Modifier, seasons: String) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(20.dp)
    ) {
        Box(
            modifier = Modifier
                .background(color = Color.Black),
            contentAlignment = Alignment.Center
        ) {
            val seasonTag = stringResource(id = R.string.tvseries_detail_screen_seasons)
            if (seasons.isNotEmpty()) {
                Text(
                    modifier = Modifier
                        .padding(start = 10.dp, end = 10.dp, top = 5.dp, bottom = 5.dp),
                    text = "$seasons $seasonTag",
                    style = MaterialTheme.typography.displaySmall
                )
            }
        }
    }
}

@Composable
private fun CastList(
    modifier: Modifier = Modifier,
    navController: NavController,
    casts: DataState<List<CastUiModel>>
) {
    when (casts) {
        is DataState.Initial -> {}
        is DataState.Loading -> {
            CircularProgress()
        }

        is DataState.Success -> {
            val cast = casts.data

            if (cast.isNotEmpty()) {
                Column(
                    modifier = modifier
                ) {
                    Text(
                        modifier = Modifier
                            .padding(10.dp),
                        text = "Cast",
                        style = MaterialTheme.typography.displayLarge
                    )
                    LazyRow(
                        modifier = Modifier,
                        horizontalArrangement = Arrangement.spacedBy(5.dp),

                        ) {
                        items(cast.size) { iterator ->
                            SingleCast(
                                modifier = Modifier
                                    .padding(start = 10.dp, end = 10.dp),
                                cast = cast[iterator],
                                navController = navController
                            )
                        }
                    }
                }
            }
        }

        is DataState.Error -> {
        }
    }
}

@Composable
private fun SingleCast(
    modifier: Modifier = Modifier,
    cast: CastUiModel,
    navController: NavController
) {
    Column(
        modifier = modifier
            .clickable {
                navController.navigate("${DetailScreens.ActorDetailScreen.route}/${cast.id}")
            },
        verticalArrangement = Arrangement.spacedBy(10.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val imageUrl = "${Constants.TMDB_IMAGE_URL}/${cast.profilePath}"
        AsyncImage(
            modifier = Modifier
                .size(85.dp)
                .clip(CircleShape)
                .shadow(elevation = 30.dp, shape = CircleShape),
            model = imageUrl,
            placeholder = painterResource(id = R.drawable.movies_dummy),
            contentDescription = null,
            contentScale = ContentScale.Crop
        )

        Text(
            text = cast.name,
            fontSize = 15.sp
        )
    }
}

@Preview
@Composable
private fun TvSeriesDetailScreenPreview() {
    TvSeriesDetailScreen(navController = rememberNavController(), tvSeriesId = 0)
}