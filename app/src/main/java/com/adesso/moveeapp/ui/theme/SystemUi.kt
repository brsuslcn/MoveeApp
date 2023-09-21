package com.adesso.moveeapp.ui.theme

import androidx.activity.ComponentActivity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.core.view.WindowCompat
import com.google.accompanist.systemuicontroller.rememberSystemUiController


object SystemUi {

    val fileSystemWindows = mutableStateOf(false)

    @Composable
    fun Color(color: Color) {
        SystemUI(color = color, fileSystemWindows = fileSystemWindows.value)
    }

    @Composable
    private fun SystemUI(color: Color, fileSystemWindows: Boolean) {
        val context = LocalContext.current
        val activity = context as? ComponentActivity
        activity?.window?.let { WindowCompat.setDecorFitsSystemWindows(it, fileSystemWindows) }
        val systemUiController = rememberSystemUiController()
        SideEffect {
            systemUiController.setStatusBarColor(color)
        }
    }
}

