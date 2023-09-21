package com.adesso.moveeapp.ui.home.movieshomescreen

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.adesso.moveeapp.R
import com.adesso.moveeapp.navigation.graphs.DetailScreens
import com.adesso.moveeapp.ui.components.AnimatedTopBar
import com.adesso.moveeapp.ui.components.CircularProgress
import com.adesso.moveeapp.ui.components.IndicatorLine
import com.adesso.moveeapp.ui.components.ItemRate
import com.adesso.moveeapp.ui.components.screenWidth
import com.adesso.moveeapp.ui.home.components.HomeBackground
import com.adesso.moveeapp.ui.home.components.SingleItemImage
import com.adesso.moveeapp.ui.home.movieshomescreen.model.MoviesPopularUiModel
import com.adesso.moveeapp.ui.home.movieshomescreen.model.MoviesStateUiModel
import com.adesso.moveeapp.ui.theme.SystemUi
import com.adesso.moveeapp.util.ItemListener


@Composable
fun MoviesHomeScreen(
    navController: NavController
) {
    val isPopularMoviesActive = remember { mutableStateOf(false) }
    SystemUi.Color(color = Color.Blue)
    LaunchedEffect(Unit) {
        SystemUi.fileSystemWindows.value = true
    }

    HomeBackground(
        modifier = Modifier.then(
            if (isPopularMoviesActive.value) {
                Modifier.background(Color.White)
            } else {
                Modifier.background(Color(0xE6003DFF))
            }
        )
    )
    MoviesHomeScreenBody(
        isPopularMoviesActive = isPopularMoviesActive,
        navController = navController
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun MoviesHomeScreenBody(
    modifier: Modifier = Modifier,
    isPopularMoviesActive: MutableState<Boolean>,
    navController: NavController,
    viewModel: MoviesHomeScreenViewModel = hiltViewModel()
) {
    val popularMovies = viewModel.moviesFlow.collectAsLazyPagingItems()
    val lazyColumnScrollState = rememberLazyListState()


    LaunchedEffect(lazyColumnScrollState) {
        snapshotFlow {
            lazyColumnScrollState.firstVisibleItemIndex
        }.collect { index ->
            when {
                index > 2 && !isPopularMoviesActive.value -> isPopularMoviesActive.value = true
                index < 2 && isPopularMoviesActive.value -> isPopularMoviesActive.value = false
            }
        }
    }

    LaunchedEffect(Unit) {
        viewModel.addFavorite(ItemListener.selectedItemId.intValue).await()
        viewModel.checkFavorite()
    }

    LazyColumn(
        modifier = modifier,
        state = lazyColumnScrollState,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(32.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(id = R.string.movies_home_screen_movies_title),
                    style = MaterialTheme.typography.titleLarge
                )
                MapButton(
                    modifier = Modifier
                        .size(30.dp)
                )
            }

        }

        item {
            PlayingMoviesList(
                modifier = Modifier
                    .padding(start = 32.dp, end = 32.dp),
                navHostController = navController,
            )
        }


        stickyHeader {
            if (isPopularMoviesActive.value) {
                AnimatedTopBar(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(70.dp)
                        .background(Color(0xff003dff))
                        .padding(bottom = 5.dp),
                    textId = R.string.movies_home_screen_movies_popular
                )
            }
        }

        when (popularMovies.loadState.refresh) {
            is LoadState.Loading -> {
                item {
                    CircularProgress()
                }
            }

            is LoadState.NotLoading -> {
                item {
                    Text(
                        modifier = Modifier
                            .padding(start = 32.dp),
                        text = stringResource(id = R.string.movies_home_screen_movies_popular),
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                items(popularMovies.itemCount) { movie ->
                    val singleMovie = popularMovies[movie]
                    SinglePopularMovie(
                        modifier = Modifier
                            .padding(start = 32.dp, end = 32.dp),
                        navHostController = navController,
                        moviePopular = singleMovie!!,
                    )
                }

                if (popularMovies.loadState.append == LoadState.Loading) {
                    item {
                        CircularProgress(
                            modifier = Modifier.padding(bottom = 30.dp)
                        )
                    }
                }
            }

            is LoadState.Error -> {}
        }
    }
}

@Composable
private fun PlayingMoviesList(
    modifier: Modifier = Modifier,
    viewModel: MoviesHomeScreenViewModel = hiltViewModel(),
    navHostController: NavController,
) {
    val lazyRowState = rememberLazyListState()
    val movies = rememberUpdatedState(viewModel.moviesPlayNowFlow.collectAsLazyPagingItems()).value
    val scrollIndex = remember { derivedStateOf { lazyRowState.firstVisibleItemIndex } }
    val playingMovieState = viewModel.playingMovieState

    LazyRow(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
        state = lazyRowState
    ) {

        items(movies.itemCount) { movie ->
            val singleMovie = movies[scrollIndex.value]
            SingleItemImage(
                modifier = Modifier
                    .width((screenWidth() * 0.7).dp)
                    .height(screenWidth().dp)
                    .clip(shape = RoundedCornerShape(15.dp))
                    .clickable {
                        val movieId = movies[movie]!!.id
                        ItemListener.selectedItemId.intValue = movieId
                        navHostController.navigate("${DetailScreens.MovieDetailScreen.route}/$movieId")
                    },
                imagePath = movies[movie]!!.imagePath
            )

            playingMovieState.value =
                MoviesStateUiModel(
                    singleMovie!!.name,
                    singleMovie.voteAverage.toString(),
                    singleMovie.genre
                )
        }
    }
    SingleMovieInfo(
        modifier = Modifier
            .padding(start = 32.dp, end = 32.dp)
    )
}

@Composable
private fun SingleMovieInfo(
    modifier: Modifier = Modifier,
    viewModel: MoviesHomeScreenViewModel = hiltViewModel()
) {
    val playingMovieState = viewModel.playingMovieState.value
    Column(
        modifier = modifier, verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        ItemRate(
            modifier = Modifier
                .size(60.dp, 25.dp)
                .clip(shape = RoundedCornerShape(20.dp))
                .background(color = Color.Blue), rate = playingMovieState.average
        )
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = playingMovieState.title,
            style = MaterialTheme.typography.displayLarge
        )
        Text(
            text = playingMovieState.genre, fontSize = 15.sp

        )
        IndicatorLine(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
        )
    }
}

@Composable
private fun SinglePopularMovie(
    modifier: Modifier = Modifier,
    navHostController: NavController,
    moviePopular: MoviesPopularUiModel,
    viewModel: MoviesHomeScreenViewModel = hiltViewModel()
) {
    val favorites by viewModel.favorites.collectAsState()
    Surface(
        modifier = modifier.clickable {
            navHostController.navigate("${DetailScreens.MovieDetailScreen.route}/${moviePopular.id}")
            ItemListener.selectedItemId.intValue = moviePopular.id
        },
        shape = RoundedCornerShape(10.dp),
        shadowElevation = 5.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(end = 15.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            SingleItemImage(
                modifier = Modifier.size(width = 70.dp, height = 100.dp),
                imagePath = moviePopular.imagePath
            )

            Column(
                verticalArrangement = Arrangement.spacedBy(5.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        modifier = Modifier.width((screenWidth() / 2).dp),
                        text = moviePopular.name,
                        style = MaterialTheme.typography.titleMedium,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    if (moviePopular.id in favorites) {
                        Icon(
                            painterResource(id = R.drawable.ic_red_heart_fill),
                            tint = Color.Red,
                            contentDescription = "Heart Icon"
                        )
                    }
                }

                Text(
                    text = moviePopular.genre,
                    fontSize = 15.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Row(
                    horizontalArrangement = Arrangement.spacedBy(5.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_calendar),
                        contentDescription = "Calendar Icon",

                        )
                    Text(
                        text = moviePopular.date,
                        style = MaterialTheme.typography.labelSmall,
                        color = Color.Gray
                    )

                    Text(
                        text = "|"
                    )

                    ItemRate(
                        modifier = Modifier
                            .size(55.dp, 22.dp)
                            .clip(shape = RoundedCornerShape(20.dp))
                            .background(color = Color.Blue),
                        rate = moviePopular.voteAverage.toString()
                    )
                }
            }
        }
    }
}

@Composable
private fun MapButton(modifier: Modifier = Modifier) {
   val context = LocalContext.current
    FloatingActionButton(
        modifier = modifier,
        shape = CircleShape,
        containerColor = Color.White,
        onClick = { openGoogleMaps(context) }
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_location),
            contentDescription = "Location Button Icon",
            tint = Color(0xFF003DFF)
        )
    }
}

fun openGoogleMaps(context: Context) {
    val gmmIntentUri = Uri.parse("geo:0,0?q=movie theater")
    val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
    mapIntent.setPackage("com.google.android.apps.maps")
    if (mapIntent.resolveActivity(context.packageManager) != null) {
        context.startActivity(mapIntent)
    }
}


@Preview
@Composable
fun MoviesHomeScreenPreview() {
    MoviesHomeScreen(rememberNavController())
}