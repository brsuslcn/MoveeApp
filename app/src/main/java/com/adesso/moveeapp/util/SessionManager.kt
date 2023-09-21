package com.adesso.moveeapp.util

import android.content.SharedPreferences
import javax.inject.Inject

class SessionManager @Inject constructor(private val sharedPreferences: SharedPreferences) {
    fun registerItem(item: String, itemId: String) {
        val editor = sharedPreferences.edit()
        editor.putString(item, itemId)
        editor.apply()
    }

    fun getRegisteredItem(item: String): String? {
        return sharedPreferences.getString(item, null)
    }
}