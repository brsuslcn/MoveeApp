package com.adesso.moveeapp.ui.home.components

import android.content.Context
import android.content.Intent
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.adesso.moveeapp.R
import com.adesso.moveeapp.ui.components.IndicatorLine
import com.adesso.moveeapp.ui.components.ItemRate
import com.adesso.moveeapp.ui.components.StatusToast
import com.adesso.moveeapp.ui.viewmodel.SharedViewModel
import com.adesso.moveeapp.util.Constants
import com.adesso.moveeapp.util.state.DataState
import kotlinx.coroutines.delay

@Composable
fun ItemCover(
    modifier: Modifier = Modifier,
    imagePath: String,
    itemRate: String,
    addFavorite: () -> Unit,
    sharedViewModel: SharedViewModel = hiltViewModel()
) {
    val isFavorite = sharedViewModel.isFavorite.collectAsState()
    val asyncImage = Constants.TMDB_IMAGE_URL + imagePath
    Box(modifier = modifier) {
        AsyncImage(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 10.dp),
            model = asyncImage,
            placeholder = painterResource(id = R.drawable.movies_dummy),
            error = painterResource(id = R.drawable.movies_dummy),
            contentDescription = "Detail Screens Image",
            contentScale = ContentScale.FillWidth
        )
        FloatingActionButton(
            modifier = Modifier
                .padding(50.dp)
                .align(Alignment.TopEnd),
            containerColor = Color.White,
            shape = CircleShape,
            onClick = {
                addFavorite()
            }
        ) {
            Icon(
                modifier = Modifier
                    .padding(15.dp)
                    .size(35.dp),
                painter = painterResource(id = if(isFavorite.value) R.drawable.ic_red_heart_fill else R.drawable.ic_heart_stroke),
                contentDescription = "Heart Icon",
                tint = Color(0xFFE74C3C)
            )
        }
        ItemRate(
            modifier = Modifier
                .size(100.dp, 25.dp)
                .align(Alignment.BottomStart)
                .padding(start = 32.dp),
            rate = itemRate
        )
    }
}

@Composable
fun ItemInfo(
    modifier: Modifier = Modifier,
    itemId: Int,
    itemType: String,
    itemOverview: String,
    itemName: String,
    itemGenre: String,
    itemDuration: String,
    itemReleaseDate: String,
    sharedViewModel: SharedViewModel = hiltViewModel()
) {

    val context = LocalContext.current
    var voteButtonState by remember { mutableStateOf(false) }
    val voteState = sharedViewModel.voteState.collectAsState()

    LaunchedEffect(Unit) {
        if (itemType == Constants.MOVIE) {
            sharedViewModel.getMovieVoteState(itemId)
        } else {
            sharedViewModel.getTvVoteState(itemId)
        }
    }

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp)

    ) {
        Text(
            text = itemName,
            style = MaterialTheme.typography.displayLarge
        )
        Text(
            text = itemGenre,
            fontSize = 15.sp,
            fontWeight = FontWeight.Normal,
            color = Color.Black

        )
        Row(
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            if (itemDuration.isNotEmpty()) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(5.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_duration),
                        contentDescription = "Duration Icon"
                    )
                    Text(
                        text = "$itemDuration min",
                        fontSize = 15.sp,
                    )
                }
                Text(
                    text = "|",
                )
            }

            if (itemReleaseDate.isNotEmpty()) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(5.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_calendar),
                        contentDescription = "Calendar Icon"
                    )
                    Text(
                        text = itemReleaseDate,
                        fontSize = 15.sp,
                    )
                }
            }
        }

        Row(
            modifier = Modifier
                .padding(top = 10.dp, bottom = 10.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            SpecialButton(
                text = "Rate (${voteState.value})",
                icon = R.drawable.ic_star_fill_24dp,
                onClick = {
                    voteButtonState = !voteButtonState
                }
            )
            if (voteButtonState) {
                IndicatorLine(
                    modifier = Modifier
                        .height(90.dp)
                        .width(1.dp)
                        .padding(bottom = 20.dp),
                    color = Color(0xFF979797),
                    isHorizontal = false
                )
                Stars(
                    modifier = Modifier
                        .padding(bottom = 20.dp),
                    vote = voteState.value,
                    itemId = itemId,
                    itemType = itemType
                )
            } else {
                SpecialButton(
                    text = "Share",
                    icon = R.drawable.ic_share,
                    onClick = {
                        val itemContent = "$itemName\n$itemReleaseDate\n$itemOverview"
                        share(context, itemContent)
                    }
                )
            }
        }

        IndicatorLine(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 5.dp)
                .height(1.dp),
            color = Color(0xFF979797)
        )
    }
}

@Composable
private fun Stars(
    modifier: Modifier = Modifier,
    itemId: Int,
    itemType: String,
    vote: Int,
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        for (i in 0 until vote) {
            AnimatedStarIcon(index = i, itemId = itemId, itemType = itemType)
        }
        for (i in 0..4 - vote) {
            AnimatedStarIcon(index = vote + i, itemId = itemId, itemType = itemType, filled = false)
        }
    }
}

@Composable
private fun AnimatedStarIcon(
    itemId: Int,
    index: Int, filled: Boolean = true,
    itemType: String,
    sharedViewModel: SharedViewModel = hiltViewModel()
) {
    var targetOpacity by remember { mutableFloatStateOf(0f) }

    LaunchedEffect(key1 = index) {
        delay((index + 1) * 100L)
        targetOpacity = 1f
    }

    val animatedOpacity by animateFloatAsState(
        targetValue = targetOpacity,
        animationSpec = tween(durationMillis = 1000)
    )

    Icon(
        modifier =
        Modifier
            .alpha(animatedOpacity)
            .clickable {
                if (itemType == Constants.MOVIE) {
                    sharedViewModel.voteMovie(itemId, index + 1)
                } else {
                    sharedViewModel.voteTv(itemId, index + 1)
                }
            },
        painter = painterResource(id = if (filled) R.drawable.ic_star_fill else R.drawable.ic_star_stroke_blue),
        contentDescription = "Star Icon",
        tint = Color.Blue
    )
}


@Composable
private fun SpecialButton(
    modifier: Modifier = Modifier,
    text: String,
    icon: Int,
    onClick: () -> Unit
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        FloatingActionButton(
            shape = CircleShape,
            containerColor = Color(0xff003dff),
            elevation = FloatingActionButtonDefaults.elevation(
                defaultElevation = 5.dp
            ),
            onClick = { onClick.invoke() }
        ) {
            Icon(
                painter = painterResource(id = icon),
                contentDescription = "Button Icon",
                tint = Color.White
            )
        }
        Text(
            text = text,
            fontSize = 15.sp
        )
    }
}


private fun share(context: Context, item: String) {
    val sendIntent: Intent = Intent().apply {
        action = Intent.ACTION_SEND
        putExtra(Intent.EXTRA_TEXT, item)
        type = "text/plain"
    }
    val shareIntent = Intent.createChooser(sendIntent, null)
    context.startActivity(shareIntent)
}

@Composable
fun FavoriteStatus(
    sharedViewModel: SharedViewModel = hiltViewModel()
) {
    val isFavoriteState = sharedViewModel.isAddedFavoriteState.collectAsState()
    when (isFavoriteState.value) {
        is DataState.Initial -> {}
        is DataState.Loading -> {}

        is DataState.Success -> {
            val messageId = (isFavoriteState.value as DataState.Success<Int>).data
            StatusToast(
                textMessage = stringResource(id = messageId)
            )
        }

        is DataState.Error -> {
            StatusToast(
                textMessage = stringResource(id = R.string.detail_screens_added_favorite_unsuccess)
            )
        }
    }
}
