package com.adesso.moveeapp.ui.home.tvserieshomescreen

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
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
import com.adesso.moveeapp.ui.components.screenHeight
import com.adesso.moveeapp.ui.components.screenWidth
import com.adesso.moveeapp.ui.home.components.HomeBackground
import com.adesso.moveeapp.ui.home.components.SingleItemImage
import com.adesso.moveeapp.ui.home.tvserieshomescreen.model.TvSeriesStateUiModel
import com.adesso.moveeapp.ui.home.tvserieshomescreen.model.TvSeriesTopRatedUiModel
import com.adesso.moveeapp.ui.theme.SystemUi
import com.adesso.moveeapp.util.ItemListener


@Composable
fun TvSeriesScreen(
    navController: NavController
) {
    val isTopRatedActive = remember { mutableStateOf(false) }
    SystemUi.Color(color = Color.Blue)
    LaunchedEffect(Unit) {
        SystemUi.fileSystemWindows.value = true
    }
    HomeBackground(
        modifier = Modifier
            .then(
                if (isTopRatedActive.value) {
                    Modifier.background(Color.White)
                } else {
                    Modifier.background(Color(0xE6003DFF))
                }
            )
    )
    TvSeriesHomeScreenBody(
        modifier = Modifier
            .padding(bottom = 32.dp),
        navController = navController,
        isTopRatedActive = isTopRatedActive
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun TvSeriesHomeScreenBody(
    modifier: Modifier = Modifier,
    viewModel: TvSeriesHomeScreenViewModel = hiltViewModel(),
    navController: NavController,
    isTopRatedActive: MutableState<Boolean>
) {
    val topRatedTvSeries = viewModel.tvSeriesTopRatedFlow.collectAsLazyPagingItems()
    val lazyColumnScrollState = rememberLazyListState()

    LaunchedEffect(Unit) {
        viewModel.addFavorite(ItemListener.selectedItemId.intValue).await()
        viewModel.checkFavorite()
    }

    LaunchedEffect(lazyColumnScrollState) {
        snapshotFlow {
            lazyColumnScrollState.firstVisibleItemIndex
        }.collect { index ->
            when {
                index > 1 && !isTopRatedActive.value -> isTopRatedActive.value = true
                index < 1 && isTopRatedActive.value -> isTopRatedActive.value = false
            }
        }
    }

    LazyColumn(
        modifier = modifier,
        state = lazyColumnScrollState,
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {

        item {
            Text(
                modifier = Modifier
                    .padding(start = 32.dp, end = 32.dp, top = 24.dp, bottom = 32.dp),
                text = stringResource(id = R.string.tvseries_home_screen_title),
                style = MaterialTheme.typography.titleLarge
            )
            PopularTvSeriesList(
                modifier = Modifier
                    .padding(start = 32.dp, end = 32.dp),
                viewModel = viewModel,
                navController = navController
            )
        }

        stickyHeader {
            if (isTopRatedActive.value) {
                AnimatedTopBar(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(70.dp)
                        .background(Color(0xff003dff))
                        .padding(bottom = 5.dp),
                    textId = R.string.tvseries_home_screen_top_rated
                )
            }
        }

        when (topRatedTvSeries.loadState.refresh) {
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
                        text = stringResource(id = R.string.tvseries_home_screen_top_rated),
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                items(topRatedTvSeries.itemCount / 2) { index ->
                    val firstItemIndex = index * 2
                    val secondItemIndex = firstItemIndex + 1

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 32.dp, end = 32.dp),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        DisplaySingleTopRatedSeries(
                            topRatedTvSeries[firstItemIndex],
                            navController,
                        )
                        Spacer(modifier = Modifier.padding(horizontal = 16.dp))
                        DisplaySingleTopRatedSeries(
                            topRatedTvSeries[secondItemIndex],
                            navController,
                        )
                    }
                }

                if (topRatedTvSeries.loadState.append == LoadState.Loading) {
                    item {
                        CircularProgress(
                            modifier = Modifier
                                .padding(bottom = 30.dp)
                        )
                    }
                }
            }

            is LoadState.Error -> {}
        }
    }
}

@Composable
private fun PopularTvSeriesList(
    modifier: Modifier = Modifier,
    viewModel: TvSeriesHomeScreenViewModel,
    navController: NavController
) {
    val popularTvSeries = viewModel.tvSeriesPopularFlow.collectAsLazyPagingItems()
    val lazyRowScrollState = rememberLazyListState()
    val lazyRowScrollIndex =
        remember { derivedStateOf { lazyRowScrollState.firstVisibleItemIndex } }

    LazyRow(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
        state = lazyRowScrollState
    ) {
        items(popularTvSeries.itemCount) { itemTvSeries ->
            val tvSeries = popularTvSeries[lazyRowScrollIndex.value]
            popularTvSeries[itemTvSeries]?.let {
                SingleItemImage(
                    modifier = Modifier
                        .width((screenWidth() * 0.6).dp)
                        .height((screenWidth() / 1.3).dp)
                        .clip(shape = RoundedCornerShape(15.dp))
                        .clickable {
                            val tvSeriesId = it.id
                            ItemListener.selectedItemId.intValue = tvSeriesId
                            navController.navigate("${DetailScreens.TvSeriesDetailScreen.route}/$tvSeriesId")
                        },
                    imagePath = it.posterPath
                )
            }

            viewModel.popularState.value =
                TvSeriesStateUiModel(
                    tvSeries!!.name,
                    tvSeries.voteAverage.toString(),
                    tvSeries.genreIds
                )
        }
    }
    SinglePopularTvSeries(
        modifier = Modifier
            .padding(start = 32.dp, end = 32.dp, top = 24.dp),
    )
}

@Composable
private fun SinglePopularTvSeries(
    modifier: Modifier = Modifier,
    viewModel: TvSeriesHomeScreenViewModel = hiltViewModel()
) {
    val popularState = viewModel.popularState.value
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        ItemRate(
            modifier = Modifier
                .size(60.dp, 25.dp)
                .clip(shape = RoundedCornerShape(20.dp))
                .background(color = Color.Blue),
            rate = popularState.tvSeriesRate
        )
        Text(
            text = popularState.tvSeriesName,
            style = MaterialTheme.typography.displayLarge
        )
        Text(
            text = popularState.tvSeriesGenre,
            fontSize = 15.sp
        )
        IndicatorLine(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
        )
    }
}

@Composable
private fun DisplaySingleTopRatedSeries(
    topRatedItem: TvSeriesTopRatedUiModel?,
    navController: NavController
) {
    topRatedItem?.let { item ->
        SingleTopRatedSeries(
            modifier = Modifier
                .width((screenWidth() * 0.3889f).dp)
                .height((screenHeight() * 0.4453f).dp)
                .clickable {
                    val tvSeriesId = item.id
                    ItemListener.selectedItemId.intValue = tvSeriesId
                    navController.navigate("${DetailScreens.TvSeriesDetailScreen.route}/$tvSeriesId")
                },
            singleTopRatedSeries = item
        )
    }
}

@Composable
private fun SingleTopRatedSeries(
    modifier: Modifier = Modifier,
    singleTopRatedSeries: TvSeriesTopRatedUiModel,
    viewModel: TvSeriesHomeScreenViewModel = hiltViewModel()
) {
    val favorites by viewModel.favorites.collectAsState()
    val imageHeight = screenHeight() * 0.342f
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(20.dp),
        shadowElevation = 5.dp
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            SingleItemImage(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(imageHeight.dp),
                imagePath = singleTopRatedSeries.imagePath
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 10.dp, end = 10.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    modifier = Modifier.width((screenWidth() / 4).dp),
                    text = singleTopRatedSeries.name,
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                if (singleTopRatedSeries.id in favorites) {
                    Icon(
                        painterResource(id = R.drawable.ic_red_heart_fill),
                        tint = Color.Red,
                        contentDescription = "Heart Icon"
                    )
                }
            }
            ItemRate(
                modifier = Modifier
                    .size(65.dp, 25.dp)
                    .padding(start = 10.dp, bottom = 5.dp),
                rate = singleTopRatedSeries.voteAverage.toString()
            )
        }
    }
}

@Preview
@Composable
private fun TvSeriesScreenPreview() {
    TvSeriesScreen(
        navController = rememberNavController()
    )
}

