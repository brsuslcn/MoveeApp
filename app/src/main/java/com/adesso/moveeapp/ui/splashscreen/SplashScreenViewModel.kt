package com.adesso.moveeapp.ui.splashscreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.adesso.moveeapp.data.repository.AccountRepository
import com.adesso.moveeapp.util.ApiResponse
import com.adesso.moveeapp.util.Constants
import com.adesso.moveeapp.util.SessionManager
import com.adesso.moveeapp.util.state.DataState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashScreenViewModel @Inject constructor(
    private val accountRepository: AccountRepository,
    private val sessionManager: SessionManager
) : ViewModel() {
    private val _isSessionValid = MutableStateFlow<DataState<Boolean>>(DataState.Loading)
    val isSessionValid: StateFlow<DataState<Boolean>> get() = _isSessionValid

    private val sessionId = sessionManager.getRegisteredItem(Constants.SESSION_ID)


    init {
        validateSession()
    }

    private fun validateSession() {
        viewModelScope.launch {
            val apiResponse = accountRepository.getAccountInfo(sessionId.toString())
            when (apiResponse) {
                is ApiResponse.Success -> {
                    _isSessionValid.value = DataState.Success(true)
                }

                is ApiResponse.Error -> {
                    _isSessionValid.value = DataState.Error(Exception("Session Error!"))
                }
            }
        }
    }
}