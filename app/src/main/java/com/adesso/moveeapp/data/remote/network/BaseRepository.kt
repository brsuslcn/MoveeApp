package com.adesso.moveeapp.data.remote.network

import com.adesso.moveeapp.util.ApiResponse
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Response

abstract class BaseRepository {
    suspend fun <T : Any> safeApiRequest(call: suspend () -> Response<T>): ApiResponse<T> {
        try {
            val response = call.invoke()
            if (response.isSuccessful) {
                val body = response.body()
                return if (body != null) {
                    ApiResponse.Success(response.body()!!)
                } else {
                    ApiResponse.Error("Response is null!")
                }
            } else {
                val error = response.errorBody()?.string()

                val message = StringBuilder()
                error?.let {
                    try {
                        message.append(JSONObject(it).getString("message"))
                    } catch (e: JSONException) {
                        return ApiResponse.Error(e.toString())
                    }

                    message.append("\n")
                }
                message.append("Error code: ${response.code()}")

                return if (response.code() >= 500) {
                    ApiResponse.Error(message.toString())
                } else {
                    ApiResponse.Error("Server Error!")
                }
            }
        } catch (e: java.lang.Exception) {
            return ApiResponse.Error("Network Error!")
        }
    }
}