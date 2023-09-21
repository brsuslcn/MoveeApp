package com.adesso.moveeapp.data.remote.network

import androidx.compose.ui.text.intl.Locale
import com.adesso.moveeapp.util.Constants
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor(private val apiKey: String) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()

        val url = originalRequest.url.newBuilder()
            .addQueryParameter("language", getLocaleLanguage())
            .build()

        val request = originalRequest.newBuilder()
            .url(url)
            .addHeader("Authorization", "Bearer $apiKey")
            .build()

        return chain.proceed(request)
    }

    private fun getLocaleLanguage(): String {
        return when(Locale.current.language) {
            Constants.TURKISH -> Constants.TURKISH
            Constants.ROMANIAN -> Constants.ROMANIAN
            else -> Constants.ENGLISH
        }
    }
}