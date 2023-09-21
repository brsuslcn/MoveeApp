package com.adesso.moveeapp.ui.components

import android.content.res.Configuration
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalConfiguration


@Composable
fun screenHeight(): Int {
    return phoneConfiguration().screenHeightDp
}

@Composable
fun screenWidth() : Int {
    return phoneConfiguration().screenWidthDp
}


@Composable
private fun phoneConfiguration() : Configuration {
    return LocalConfiguration.current
}
