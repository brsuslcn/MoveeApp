package com.adesso.moveeapp.ui.home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import coil.compose.AsyncImage
import com.adesso.moveeapp.R
import com.adesso.moveeapp.util.Constants

@Composable
fun HomeBackground(modifier: Modifier = Modifier) {
    Column {
        Box(
            modifier = modifier
                .fillMaxWidth()
                .weight(1f)
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(2.5f)
                .background(color = Color.White)
        )

    }
}

@Composable
fun SingleItemImage(modifier: Modifier = Modifier, imagePath: String) {
    val imageUrl = Constants.TMDB_IMAGE_URL + imagePath
    AsyncImage(
        modifier = modifier,
        model = imageUrl,
        placeholder = painterResource(id = R.drawable.movies_dummy),
        contentDescription = "network image",
        error = painterResource(id = R.drawable.movies_dummy),
        contentScale = ContentScale.Crop
    )
}