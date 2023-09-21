package com.adesso.moveeapp.util

import retrofit2.Response

sealed class ApiResponse<out T> {
    data class Success<out T>(val data: T) : ApiResponse<T>()
    data class Error(val exception: String) : ApiResponse<Nothing>()
}
