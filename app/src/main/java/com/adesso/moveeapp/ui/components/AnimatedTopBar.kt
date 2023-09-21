package com.adesso.moveeapp.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

@Composable
fun AnimatedTopBar(modifier: Modifier = Modifier, textId: Int) {
    var targetOpacity by remember { mutableFloatStateOf(0f) }
    LaunchedEffect(key1 = true) {
        delay(300)
        targetOpacity = 1f
    }
    val animatedOpacity by animateFloatAsState(
        targetValue = targetOpacity,
        animationSpec = tween(durationMillis = 2000)
    )

    Box(
        modifier = modifier
            .alpha(animatedOpacity),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = stringResource(id = textId),
            fontSize = 17.sp,
            color = Color.White
        )
    }
}