package com.adesso.moveeapp.ui.loginscreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.adesso.moveeapp.util.ApiResponse
import com.adesso.moveeapp.data.model.authmodel.AuthCreateSessionModel
import com.adesso.moveeapp.data.model.authmodel.AuthSessionRequestModel
import com.adesso.moveeapp.data.model.authmodel.AuthTokenModel
import com.adesso.moveeapp.data.model.authmodel.AuthUserModel
import com.adesso.moveeapp.data.repository.AccountRepository
import com.adesso.moveeapp.data.repository.AuthRepository
import com.adesso.moveeapp.util.Constants
import com.adesso.moveeapp.util.SessionManager
import com.adesso.moveeapp.util.state.DataState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginScreenViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val accountRepository: AccountRepository,
    private val sessionManager: SessionManager,

    ) : ViewModel() {

    private var requestToken = ""
    private val _loginResult = MutableStateFlow<DataState<String>>(DataState.Initial)
    val loginResult: StateFlow<DataState<String>> = _loginResult


    private fun getNewToken() : Deferred<Unit> = viewModelScope.async {
            val apiResponse = authRepository.getRequestKey()
            when(apiResponse) {
                is ApiResponse.Success -> {
                    val token : AuthTokenModel = apiResponse.data
                    requestToken = token.requestToken
                }

                is ApiResponse.Error -> {
                    _loginResult.value = DataState.Error(Exception("RequestToken Error"))
                    _loginResult.value = DataState.Initial
                }
            }
    }

    private fun createSessionId(requestToken: String) {
        viewModelScope.launch {
            val newRequestToken = AuthSessionRequestModel(requestToken)

            val apiResponse = authRepository.createSessionId(newRequestToken)
            when(apiResponse) {
                is ApiResponse.Success -> {
                    val response : AuthCreateSessionModel = apiResponse.data
                    val sessionId = response.sessionId
                    _loginResult.value = DataState.Success(sessionId)
                }

                is ApiResponse.Error -> {
                    _loginResult.value = DataState.Error(Exception("SessionId error!"))
                    _loginResult.value = DataState.Initial
                }
            }
        }
    }

    fun login(user: AuthUserModel) {
        viewModelScope.launch {
            _loginResult.value = DataState.Loading
            val requestTokenJob = getNewToken()
            requestTokenJob.await()

            val apiResponse = authRepository.login(requestToken, user)
            when(apiResponse) {
                is ApiResponse.Success -> { createSessionId(requestToken) }
                is ApiResponse.Error -> {
                    _loginResult.value = DataState.Error(Exception("Invalid username or password!"))}
            }
        }
    }

    fun registerAccountId(sessionId: String) {
        viewModelScope.launch {
            val apiResponse = accountRepository.getAccountInfo(sessionId)

            when(apiResponse) {
                is ApiResponse.Success -> {
                    val id = apiResponse.data.id
                    registerItem(Constants.PROFILE_ID, id.toString())
                }

                is ApiResponse.Error -> {}
            }
        }
    }

    fun registerItem(item: String, itemId: String) {
        sessionManager.registerItem(item, itemId)
    }
}