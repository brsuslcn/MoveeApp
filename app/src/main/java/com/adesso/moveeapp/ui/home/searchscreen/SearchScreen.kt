package com.adesso.moveeapp.ui.home.searchscreen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
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
import com.adesso.moveeapp.ui.components.CircularProgress
import com.adesso.moveeapp.ui.components.screenHeight
import com.adesso.moveeapp.ui.components.screenWidth
import com.adesso.moveeapp.ui.home.components.SingleItemImage
import com.adesso.moveeapp.ui.home.searchscreen.model.MediaTypeUiModel
import com.adesso.moveeapp.ui.home.searchscreen.model.SearchUiModel
import com.adesso.moveeapp.ui.theme.SystemUi
import com.adesso.moveeapp.util.Constants
import com.adesso.moveeapp.util.ItemListener

@Composable
fun SearchScreen(
    navController: NavController,
) {

    SystemUi.Color(color = Color.Blue)
    LaunchedEffect(Unit) {
        SystemUi.fileSystemWindows.value = true
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.White)
            .padding(bottom = 30.dp),
    ) {
        SearchBar(
            modifier = Modifier

                .background(color = Color(0xE6003DFF))
                .padding(32.dp)
                .fillMaxWidth()
                .height((screenHeight() * 0.20f).dp)


        )
        SearchList(
            modifier = Modifier
                .fillMaxWidth()
                .background(color = Color.White)
                .padding(24.dp),
            navController = navController
        )
    }
}

@Composable
private fun SearchBar(
    modifier: Modifier = Modifier,
    viewModel: SearchScreenViewModel = hiltViewModel()
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = stringResource(id = R.string.search_screen_title),
            style = MaterialTheme.typography.titleLarge
        )

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(13.dp)
        ) {
            SearchField()
            Text(
                modifier = Modifier
                    .clickable {
                        viewModel.searchState.value = ""
                    },
                text = stringResource(id = R.string.search_screen_cancel),
                color = Color.White,
                fontSize = 17.sp
            )
        }
    }
}

@Composable
private fun SearchField(
    modifier: Modifier = Modifier,
    viewModel: SearchScreenViewModel = hiltViewModel()
) {
    Box(
        modifier = modifier
            .shadow(10.dp)
            .size(
                (screenWidth() * 0.602f).dp,
                (screenHeight() * 0.0562f).dp
            )
            .background(
                color = Color.White,
                shape = RoundedCornerShape(15.dp)
            ),
        contentAlignment = Alignment.CenterStart
    ) {
        Row(
            modifier = modifier,
            horizontalArrangement = Arrangement.spacedBy(5.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                modifier = Modifier
                    .padding(start = 10.dp),
                painter = painterResource(id = R.drawable.ic_mini_search),
                contentDescription = "Search Icon"
            )
            BasicTextField(
                modifier = Modifier
                    .padding(end = 35.dp),
                value = viewModel.searchState.value,
                onValueChange = { newValue -> viewModel.searchState.value = newValue },
                singleLine = true,
                textStyle = TextStyle(
                    fontSize = 17.sp
                )
            )
        }

        if (viewModel.searchState.value.isNotEmpty()) {
            IconButton(
                modifier = Modifier
                    .align(Alignment.CenterEnd),
                onClick = {
                    viewModel.searchState.value = ""
                }
            ) {
                Image(
                    imageVector = ImageVector.vectorResource(id = R.drawable.ic_mini_x),
                    contentDescription = "X Icon",
                )
            }
        }
    }
}

@Composable
private fun SearchList(
    modifier: Modifier = Modifier,
    viewModel: SearchScreenViewModel = hiltViewModel(),
    navController: NavController
) {
    val searchResults = viewModel.searchResultFlow.collectAsLazyPagingItems()
    val searchState = viewModel.searchState

    LaunchedEffect(viewModel.searchState.value) {
        val searchValue = viewModel.searchState.value
        if (searchValue.isEmpty()) {
            viewModel.setQuery()
            return@LaunchedEffect
        }

        if (searchValue.length < 4) {
            return@LaunchedEffect
        }
        viewModel.setQuery()
    }

    LazyColumn(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        contentPadding = PaddingValues(bottom = 24.dp)
    ) {

        when (searchResults.loadState.refresh) {
            is LoadState.Loading -> {
                if (searchState.value.isNotEmpty()) {
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxSize(),
                            contentAlignment = Alignment.TopCenter
                        ) {
                            CircularProgressIndicator()
                        }
                    }
                }
            }

            is LoadState.NotLoading -> {
                if (searchState.value.isNotEmpty() && searchResults.itemCount == 0) {
                    item {
                        NotFound(
                            modifier = Modifier
                                .fillMaxSize()
                        )
                    }
                }
                items(searchResults.itemCount) { searchResult ->
                    val searchItem = searchResults[searchResult]
                    SingleSearchItem(
                        modifier = Modifier
                            .width((screenWidth() * 0.869f).dp)
                            .height((screenHeight() * 0.156f).dp),
                        navController = navController,
                        searchResult = searchItem!!
                    )
                }
                if (searchResults.loadState.append == LoadState.Loading) {
                    item {
                        CircularProgress(
                            modifier = Modifier.padding()
                        )
                    }
                }
            }

            is LoadState.Error -> {
                item {
                    NotFound(
                        modifier = Modifier
                            .fillMaxSize()
                    )
                }
            }
        }
    }
}

@Composable
private fun SingleSearchItem(
    modifier: Modifier = Modifier,
    navController: NavController,
    searchResult: SearchUiModel
) {
    val itemType = getMediaType(searchResult.itemType)
    Surface(
        modifier = modifier
            .clickable {
                if(itemType.type == Constants.TV || itemType.type == Constants.MOVIE)
                    ItemListener.selectedItemId.intValue = searchResult.itemId
                itemType.navigate(searchResult.itemId, navController)
            },
        shadowElevation = 5.dp,
        shape = RoundedCornerShape(10.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            val image = Constants.TMDB_IMAGE_URL + searchResult.itemImage
            SingleItemImage(
                modifier = Modifier
                    .width((screenWidth() * 0.194f).dp)
                    .fillMaxHeight(),
                imagePath = image
            )

            Column(
                verticalArrangement = Arrangement.spacedBy(5.dp)
            ) {
                Text(
                    text = searchResult.itemName,
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Text(
                    text = searchResult.itemDescription,
                    fontSize = 15.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Row(
                    horizontalArrangement = Arrangement.spacedBy(5.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = painterResource(id = itemType.iconResId),
                        contentDescription = "Search Result Icon",
                    )
                    Text(
                        text = stringResource(id = itemType.stringResourceId),
                        style = MaterialTheme.typography.labelSmall,
                        color = Color.Gray
                    )
                }
            }
        }
    }
}

@Composable
private fun NotFound(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_search_result_not_found),
            contentDescription = "Not Found Icon"
        )
        Text(
            modifier = Modifier
                .fillMaxWidth(),
            text = stringResource(id = R.string.search_screen_result_not_found),
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp,
            color = Color(0xff0432ff)
        )

    }
}

fun getMediaType(type: String): MediaTypeUiModel {
    return when (type) {
        Constants.MOVIE -> MediaTypeUiModel.Movie
        Constants.TV -> MediaTypeUiModel.TvSeries
        Constants.PERSON -> MediaTypeUiModel.Person
        else -> throw IllegalArgumentException("Unknown media type: $type")
    }
}

@Preview
@Composable
private fun SearchScreenPreview() {
    SearchScreen(navController = rememberNavController())

}