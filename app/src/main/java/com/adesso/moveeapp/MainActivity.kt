package com.adesso.moveeapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.rememberNavController
import com.adesso.moveeapp.navigation.graphs.RootNavGraph
import com.adesso.moveeapp.ui.theme.MoveeAppTheme
import com.adesso.moveeapp.util.Constants
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val extras = intent.extras
        val isSessionValid = extras?.getBoolean(Constants.IS_SESSION_VALID) ?: false

        setContent {
            MoveeAppTheme {
                RootNavGraph(
                    navController = rememberNavController(),
                    isSessionValid = isSessionValid
                )
            }
        }
    }
}