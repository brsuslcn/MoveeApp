package com.adesso.moveeapp.data.repository

import com.adesso.moveeapp.data.model.authmodel.AuthCreateSessionModel
import com.adesso.moveeapp.data.model.authmodel.AuthLoginModel
import com.adesso.moveeapp.data.model.authmodel.AuthSessionRequestModel
import com.adesso.moveeapp.data.model.authmodel.AuthTokenModel
import com.adesso.moveeapp.data.model.authmodel.AuthUserModel
import com.adesso.moveeapp.data.remote.network.BaseRepository
import com.adesso.moveeapp.data.remote.service.AuthService
import com.adesso.moveeapp.util.ApiResponse
import javax.inject.Inject


class AuthRepository @Inject constructor(private val authServices: AuthService) : BaseRepository() {
    suspend fun getRequestKey(): ApiResponse<AuthTokenModel> {
        return safeApiRequest { authServices.getRequestKey() }
    }

    suspend fun login(requestToken: String, user: AuthUserModel): ApiResponse<AuthLoginModel> {
        return safeApiRequest { authServices.loginValidation(requestToken, user) }
    }

    suspend fun createSessionId(requestToken: AuthSessionRequestModel): ApiResponse<AuthCreateSessionModel> {
        return safeApiRequest { authServices.createSessionId(requestToken) }
    }
}