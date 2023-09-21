package com.adesso.moveeapp.ui.home.profilescreen

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.adesso.moveeapp.R
import com.adesso.moveeapp.data.model.addfavoritesmodel.AddFavoritesBodyModel
import com.adesso.moveeapp.navigation.graphs.DetailScreens
import com.adesso.moveeapp.ui.components.CircularProgress
import com.adesso.moveeapp.ui.components.screenHeight
import com.adesso.moveeapp.ui.components.screenWidth
import com.adesso.moveeapp.ui.home.components.SingleItemImage
import com.adesso.moveeapp.ui.home.profilescreen.model.FavoriteUiModel
import com.adesso.moveeapp.ui.theme.SystemUi
import com.adesso.moveeapp.util.Constants
import com.adesso.moveeapp.util.state.DataState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    navController: NavController,
    viewModel: ProfileScreenViewModel = hiltViewModel()
) {
    val usernameState = viewModel.username.collectAsState().value

    SystemUi.Color(color = Color.Blue)
    LaunchedEffect(Unit) {
        viewModel.getAccountInfo()
        SystemUi.fileSystemWindows.value = true
    }

    when (usernameState) {
        is DataState.Initial -> {}
        is DataState.Loading -> {
            CircularProgress()
        }

        is DataState.Success -> {
            val username = usernameState.data
            Scaffold { paddingValues ->
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(color = Color.White)
                        .padding(bottom = paddingValues.calculateBottomPadding().value.dp)
                ) {
                    ProfileDetail(
                        modifier = Modifier
                            .background(color = Color(0xE6003DFF))
                            .padding(32.dp)
                            .fillMaxWidth()
                            .height((screenHeight() * 0.20f).dp),
                        username = username
                    )

                    FavoriteList(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp),
                        navController = navController
                    )
                }
            }
        }

        is DataState.Error -> {}
    }
}

@Composable
private fun ProfileDetail(
    modifier: Modifier = Modifier,
    username: String
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = stringResource(id = R.string.profile_screen_title),
                style = MaterialTheme.typography.titleLarge,
                color = Color.White
            )
            LogoutButton()
        }

        Column(
            verticalArrangement = Arrangement.spacedBy(5.dp)
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(5.dp)
            ) {
                Text(
                    text = stringResource(id = R.string.profile_screen_hello),
                    fontSize = 17.sp,
                    color = Color.White
                )
                Image(
                    painter = painterResource(id = R.drawable.ic_hello),
                    contentDescription = "Hello Icon"
                )
            }
            Text(
                text = username,
                fontWeight = FontWeight.Bold,
                fontSize = 25.sp,
                color = Color.White
            )
        }
    }
}

@Composable
private fun LogoutButton(
    modifier: Modifier = Modifier,
    viewModel: ProfileScreenViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val isLogout = viewModel.isLogout.collectAsState()

    FloatingActionButton(
        modifier = modifier,
        containerColor = Color.White,
        shape = CircleShape,
        onClick = {
            viewModel.showDialogState.value = true
        }
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_logout),
            contentDescription = "Logout Icon"
        )
    }
    if (viewModel.showDialogState.value) {
        PopUpExit()
    }

    LaunchedEffect(isLogout.value) {
        if (isLogout.value)
            restartMainActivity(context)
    }
}

@Composable
private fun PopUpExit(viewModel: ProfileScreenViewModel = hiltViewModel()) {
    if (viewModel.showDialogState.value) {
        AlertDialog(
            onDismissRequest = {
                viewModel.showDialogState.value = false
            },
            title = {
                Text(stringResource(id = R.string.profile_screen_exit_alertdialog_title))
            },
            text = {
                Text(stringResource(id = R.string.profile_screen_exit_alerdialog_content))
            },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.showDialogState.value = false
                        viewModel.logout()
                    }
                ) {
                    Text(stringResource(id = R.string.profile_screen_exit_alertdialog_button_yes))
                }

            },

            dismissButton = {
                Button(
                    onClick = {
                        viewModel.showDialogState.value = false
                    }
                ) {
                    Text(stringResource(id = R.string.profile_screen_exit_alerdialog_button_no))
                }
            }

        )
    }
}

@Composable
private fun FavoriteList(
    modifier: Modifier = Modifier,
    navController: NavController,
    viewModel: ProfileScreenViewModel = hiltViewModel(),
) {
    val favoriteTv = viewModel.favoriteTvFlow.collectAsLazyPagingItems()
    val favoriteMovie = viewModel.favoriteMovieFlow.collectAsLazyPagingItems()

    DisposableEffect(Unit) {
        onDispose {
            favoriteTv.refresh()
            favoriteMovie.refresh()
        }
    }

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Text(
            text = stringResource(id = R.string.profile_screen_favorite),
            fontWeight = FontWeight.Bold,
            fontSize = 22.sp
        )
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            contentPadding = PaddingValues(bottom = 24.dp)
        ) {
            items(favoriteMovie.itemCount) { movie ->
                SingleFavorite(
                    item = favoriteMovie[movie]!!,
                    navController = navController,
                    mediaType = Constants.MOVIE
                )
            }

            items(favoriteTv.itemCount) { tv ->
                SingleFavorite(
                    item = favoriteTv[tv]!!,
                    navController = navController,
                    mediaType = Constants.TV
                )
            }
        }
    }

    PagingItemsRefresh(favoriteMovie = favoriteMovie, favoriteTv = favoriteTv)
}

@Composable
private fun PagingItemsRefresh(
    favoriteMovie: LazyPagingItems<FavoriteUiModel>,
    favoriteTv: LazyPagingItems<FavoriteUiModel>,
    viewModel: ProfileScreenViewModel = hiltViewModel()
) {
    val refreshState = viewModel.lazyColumnRefreshState
    if (refreshState.value) {
        favoriteMovie.refresh()
        favoriteTv.refresh()
        refreshState.value = false
    }
}

@Composable
private fun SingleFavorite(
    modifier: Modifier = Modifier,
    navController: NavController,
    item: FavoriteUiModel,
    mediaType: String,
    viewModel: ProfileScreenViewModel = hiltViewModel()
) {
    Surface(
        modifier = modifier
            .width((screenWidth() * 0.869f).dp)
            .height((screenHeight() * 0.156f).dp)
            .clickable {
                navController.navigate(
                    route =
                    if (mediaType == Constants.MOVIE) {
                        "${DetailScreens.MovieDetailScreen.route}/${item.id}"
                    } else {
                        "${DetailScreens.TvSeriesDetailScreen.route}/${item.id}"
                    }
                )
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
            val image = Constants.TMDB_IMAGE_URL + item.imagePath
            SingleItemImage(
                modifier = Modifier
                    .width((screenWidth() * 0.194f).dp)
                    .fillMaxHeight(),
                imagePath = image
            )

            Column(
                verticalArrangement = Arrangement.spacedBy(5.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        modifier = Modifier
                            .width((screenWidth() / 2).dp),
                        text = item.name,
                        style = MaterialTheme.typography.titleMedium,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Icon(
                        modifier = Modifier
                            .clickable {
                                val deleteFavorite =
                                    AddFavoritesBodyModel(mediaType, item.id, false)
                                viewModel.deleteFavorite(deleteFavorite)
                            },
                        painter = painterResource(id = R.drawable.ic_red_heart_fill),
                        tint = Color.Red,
                        contentDescription = "Heart Icon"
                    )
                }
                Text(
                    text = item.character,
                    fontSize = 15.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                if(item.releaseYear!="") {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(5.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.ic_calendar),
                            contentDescription = "Search Result Icon",
                        )
                        Text(
                            text = item.releaseYear,
                            style = MaterialTheme.typography.labelSmall,
                            color = Color.Gray
                        )
                    }
                }
            }
        }
    }
}

private fun restartMainActivity(context: Context) {
    val intent = Intent(context, context::class.java)
    (context as? Activity)?.finish()
    context.startActivity(intent)
}

@Preview
@Composable
private fun ProfileScreenPreview() {
    ProfileScreen(rememberNavController())
}