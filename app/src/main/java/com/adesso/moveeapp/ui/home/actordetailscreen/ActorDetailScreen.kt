package com.adesso.moveeapp.ui.home.actordetailscreen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
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
import coil.compose.AsyncImage
import com.adesso.moveeapp.R
import com.adesso.moveeapp.navigation.graphs.DetailScreens
import com.adesso.moveeapp.ui.components.CircularProgress
import com.adesso.moveeapp.ui.components.ItemRate
import com.adesso.moveeapp.ui.components.screenWidth
import com.adesso.moveeapp.ui.home.actordetailscreen.model.ActorCreditUiModel
import com.adesso.moveeapp.ui.theme.SystemUi
import com.adesso.moveeapp.util.Constants
import com.adesso.moveeapp.util.state.DataState


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ActorDetailScreen(
    navController: NavController,
    actorId: Int,
    viewModel: ActorDetailScreenViewModel = hiltViewModel()
) {
    val actorDetail = viewModel.actorDetailFlow.collectAsState().value
    val tvSeriesCredits = viewModel.tvCreditsFlow.collectAsState().value
    val movieCredits = viewModel.movieCreditsFlow.collectAsState().value

    SystemUi.Color(color = Color.Transparent)
    LaunchedEffect(Unit) {
        SystemUi.fileSystemWindows.value = false
    }

    LaunchedEffect(Unit) {
        viewModel.getActorDetail(actorId)
        viewModel.getTvCredits(actorId)
        viewModel.getMovieCredits(actorId)
    }

    when (actorDetail) {
        is DataState.Initial -> {}

        is DataState.Loading -> {
            CircularProgress()
        }

        is DataState.Success -> {
            val actor = actorDetail.data
            Scaffold { paddingValues ->
                LazyColumn(
                    modifier = Modifier
                        .padding(bottom = paddingValues.calculateBottomPadding().value.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    item {
                        val imagePath = "${Constants.TMDB_IMAGE_URL}/${actor.image}"
                        AsyncImage(
                            modifier = Modifier
                                .fillMaxWidth(),
                            model = imagePath,
                            placeholder = painterResource(id = R.drawable.movies_dummy),
                            contentDescription = "Actor Image",
                            error = painterResource(id = R.drawable.movies_dummy),
                            contentScale = ContentScale.FillWidth
                        )
                    }
                    item {
                        Text(
                            modifier = Modifier
                                .padding(start = 32.dp, end = 32.dp),
                            text = actor.name,
                            style = MaterialTheme.typography.displayLarge
                        )
                    }
                    item {
                        ActorBio(
                            modifier = Modifier
                                .padding(start = 32.dp, end = 32.dp),
                            biography = actor.biography
                        )
                        ActorBornStatus(
                            modifier = Modifier
                                .padding(start = 32.dp, top = 8.dp),
                            born = actor.born
                        )
                    }

                    when (movieCredits) {
                        is DataState.Initial -> {}
                        is DataState.Loading -> {}
                        is DataState.Error -> {}
                        is DataState.Success -> {
                            val credits = movieCredits.data
                            items(credits.size) { credit ->
                                SingleArtistCreditItem(
                                    modifier = Modifier
                                        .padding(start = 32.dp, end = 32.dp),
                                    credit = credits[credit],
                                    navController = navController,
                                    isMovie = true
                                )
                            }
                        }
                    }
                    when (tvSeriesCredits) {
                        is DataState.Initial -> {}
                        is DataState.Loading -> {}
                        is DataState.Error -> {}
                        is DataState.Success -> {
                            val credits = tvSeriesCredits.data
                            items(credits.size) { credit ->
                                SingleArtistCreditItem(
                                    modifier = Modifier
                                        .padding(start = 32.dp, end = 32.dp),
                                    credit = credits[credit],
                                    navController = navController,
                                    isMovie = false
                                )
                            }

                            item {
                                Spacer(modifier = Modifier.padding(16.dp))
                            }
                        }
                    }
                }
            }
        }

        is DataState.Error -> {
            Text("An error occurred, please try again!")
        }
    }
}


@Composable
private fun ActorBio(modifier: Modifier = Modifier, biography: String) {
    val maxLineState = remember { mutableIntStateOf(3) }

    val seeFullString = stringResource(id = R.string.actor_detail_screen_see_full_bio)
    val seeLessString = stringResource(id = R.string.actor_detail_screen_see_less_bio)
    val bioTextState = remember { mutableStateOf(seeFullString) }

    val totalLines = remember {
        mutableIntStateOf(
            if (biography.isNotEmpty()) {
                biography.length
            } else {
                1
            }
        )
    }


    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        if (biography.isNotEmpty()) {
            Text(
                text = biography,
                maxLines = maxLineState.intValue,
                fontSize = 17.sp,
            )
            if (maxLineState.intValue == 3) {
                Text("...")
            }
            Text(
                modifier = Modifier
                    .clickable {
                        if (maxLineState.intValue == 3) {
                            maxLineState.intValue = totalLines.intValue
                            bioTextState.value = seeLessString
                        } else {
                            maxLineState.intValue = 3
                            bioTextState.value = seeFullString
                        }
                    },
                text = bioTextState.value,
                fontSize = 17.sp,
                color = Color(0xff003dff)
            )
        }
    }
}

@Composable
private fun ActorBornStatus(modifier: Modifier = Modifier, born: String) {
    if (born.isNotEmpty()) {
        Row(
            modifier = modifier,
            horizontalArrangement = Arrangement.spacedBy(5.dp)
        ) {
            Text(
                text = stringResource(id = R.string.actor_detail_screen_born),
                fontWeight = FontWeight.Bold,
                fontSize = 17.sp
            )
            Text(
                text = born,
                fontSize = 17.sp
            )
        }
    }
}

@Composable
private fun SingleArtistCreditItem(
    modifier: Modifier = Modifier,
    navController: NavController,
    credit: ActorCreditUiModel,
    isMovie: Boolean
) {
    Surface(
        modifier = modifier
            .clickable {
                if (isMovie) {
                    navController.navigate("${DetailScreens.MovieDetailScreen.route}/${credit.id}")
                } else {
                    navController.navigate("${DetailScreens.TvSeriesDetailScreen.route}/${credit.id}")
                }
            },
        shadowElevation = 5.dp,
        shape = RoundedCornerShape(10.dp),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            val imagePath = "${Constants.TMDB_IMAGE_URL}/${credit.imagePath}"
            AsyncImage(
                modifier = Modifier
                    .width(
                        width = (screenWidth() * 0.194f).dp,
                    )
                    .fillMaxHeight(),
                model = imagePath,
                placeholder = painterResource(id = R.drawable.movies_dummy),
                error = painterResource(id = R.drawable.movies_dummy),
                contentDescription = null
            )

            Column(
                verticalArrangement = Arrangement.spacedBy(5.dp)
            ) {
                Text(
                    text = credit.name,
                    style = MaterialTheme.typography.titleMedium,
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
                        text = credit.date,
                        style = MaterialTheme.typography.labelSmall,
                        color = Color.Gray
                    )

                    Text(
                        text = "|"
                    )

                    ItemRate(
                        modifier = Modifier
                            .size(55.dp, 25.dp)
                            .clip(shape = RoundedCornerShape(20.dp))
                            .background(color = Color.Blue),
                        rate = credit.voteAverage.toString()
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun ActorDetailScreenPreview() {
    ActorDetailScreen(rememberNavController(), 1223786)
}