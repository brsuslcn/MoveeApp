package com.adesso.moveeapp.util.state

import java.lang.Exception

sealed class DataState<out T>  {

    object Initial : DataState<Nothing>()
    object Loading : DataState<Nothing>()
    data class Success<T>(val data: T) : DataState<T>()
    data class Error(val exception: Exception) : DataState<Nothing>()
}