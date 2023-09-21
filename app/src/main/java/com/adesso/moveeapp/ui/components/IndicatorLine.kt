package com.adesso.moveeapp.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.unit.dp

@Composable
fun IndicatorLine(
    modifier: Modifier = Modifier,
    color: Color = Color(0x4cabb4bd),
    isHorizontal: Boolean = true
) {
    Canvas(
        modifier = modifier
    ) {
        drawLine(
            color = color,
            start = Offset(0f, 0f),
            end = if(isHorizontal) Offset(size.width, 0f) else Offset(0f, size.height) ,
            strokeWidth = 1.dp.toPx(),
            cap = StrokeCap.Butt
        )
    }
}