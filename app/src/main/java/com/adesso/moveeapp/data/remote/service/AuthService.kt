package com.adesso.moveeapp.data.remote.service

import com.adesso.moveeapp.data.model.authmodel.AuthCreateSessionModel
import com.adesso.moveeapp.data.model.authmodel.AuthLoginModel
import com.adesso.moveeapp.data.model.authmodel.AuthSessionRequestModel
import com.adesso.moveeapp.data.model.authmodel.AuthTokenModel
import com.adesso.moveeapp.data.model.authmodel.AuthUserModel
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query


interface AuthService {
    @GET(Constants.NEW_REQUEST_TOKEN_PATH)
    suspend fun getRequestKey() : Response<AuthTokenModel>

    @POST(Constants.LOGIN_VALIDATE_PATH)
    suspend fun loginValidation(
        @Query("request_token") requestToken: String,
        @Body user: AuthUserModel) : Response<AuthLoginModel>

    @POST(Constants.GET_SESSION_ID_PATH)
    suspend fun createSessionId(
        @Body requestToken: AuthSessionRequestModel) : Response<AuthCreateSessionModel>
}