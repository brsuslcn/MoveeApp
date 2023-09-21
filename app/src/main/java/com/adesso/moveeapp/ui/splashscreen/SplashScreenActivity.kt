package com.adesso.moveeapp.ui.splashscreen

import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.viewModels
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import com.adesso.moveeapp.MainActivity
import com.adesso.moveeapp.util.Constants
import com.adesso.moveeapp.util.state.DataState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SplashScreenActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val splashScreen = installSplashScreen()
            splashScreen.setKeepOnScreenCondition { true }
        }

        val viewModel: SplashScreenViewModel by viewModels()

        lifecycleScope.launch {
            viewModel.isSessionValid.collect { isSessionValid ->
                when (isSessionValid) {
                    is DataState.Initial -> {}

                    is DataState.Loading -> {}

                    is DataState.Success -> {
                        navigateToActivity(true)
                    }

                    is DataState.Error -> {
                        navigateToActivity(false)
                    }
                }
            }
        }
    }

    private fun navigateToActivity(isSessionValid: Boolean) {
        val intent = Intent(this@SplashScreenActivity, MainActivity::class.java)
        intent.putExtra(Constants.IS_SESSION_VALID, isSessionValid)
        startActivity(intent)
        finish()
    }
}