package com.adesso.moveeapp.ui.components

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

@Composable
fun StatusToast(textMessage: String) {
    val context = LocalContext.current
    Toast.makeText(context, textMessage, Toast.LENGTH_SHORT).show()
}