package com.adesso.moveeapp

import com.adesso.moveeapp.data.model.authmodel.AuthCreateSessionModel
import com.adesso.moveeapp.data.model.authmodel.AuthLoginModel
import com.adesso.moveeapp.data.model.authmodel.AuthSessionRequestModel
import com.adesso.moveeapp.data.model.authmodel.AuthTokenModel
import com.adesso.moveeapp.data.model.authmodel.AuthUserModel
import com.adesso.moveeapp.data.repository.AccountRepository
import com.adesso.moveeapp.data.repository.AuthRepository
import com.adesso.moveeapp.ui.loginscreen.LoginScreenViewModel
import com.adesso.moveeapp.util.ApiResponse
import com.adesso.moveeapp.util.SessionManager
import com.adesso.moveeapp.util.state.DataState
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class LoginScreenViewModelTest {

    private val mockkAuthRepository = mockk<AuthRepository>()
    private val mockkAccountRepository = mockk<AccountRepository>()
    private val mockkSessionManager = mockk<SessionManager>()

    private lateinit var viewModel: LoginScreenViewModel

    private val user = AuthUserModel("testuser", "testpassword")
    private val requestToken = "test_request_token"
    private val testSessionId = "session_id_test"


    @Before
    fun setup() {
        MockKAnnotations.init(this)
        viewModel = LoginScreenViewModel(
            accountRepository = mockkAccountRepository,
            authRepository = mockkAuthRepository,
            sessionManager = mockkSessionManager
        )
        Dispatchers.setMain(Dispatchers.Unconfined)
    }

    @After
    fun cleanup() {
        Dispatchers.resetMain()
    }

    @Test
    fun `login success`() = runBlocking {
        coEvery { mockkAuthRepository.getRequestKey() } returns ApiResponse.Success(
            AuthTokenModel(
                success = true,
                requestToken = requestToken,
                expiresAt = "test_date_hour"
            )
        )

        coEvery {
            mockkAuthRepository.login(
                requestToken = requestToken,
                user = user
            )
        } returns ApiResponse.Success(
            AuthLoginModel(
                success = true,
                requestToken = requestToken,
                expiresAt = "test_date_hour"
            )
        )

        coEvery { mockkAuthRepository.createSessionId(AuthSessionRequestModel(requestToken)) } returns ApiResponse.Success(
            AuthCreateSessionModel(sessionId = testSessionId, success = true)
        )

        viewModel.login(user = user)

        coVerify(exactly = 1) { mockkAuthRepository.getRequestKey() }
        coVerify(exactly = 1) { mockkAuthRepository.login(requestToken, user) }
        coVerify(exactly = 1) {
            mockkAuthRepository.createSessionId(
                AuthSessionRequestModel(
                    requestToken
                )
            )
        }

        assert(viewModel.loginResult.value == DataState.Success(testSessionId))
    }

    @Test
    fun `when user login with wrong username or password`() = runBlocking {
        coEvery { mockkAuthRepository.getRequestKey() } returns ApiResponse.Success(
            AuthTokenModel(
                success = true,
                requestToken = requestToken,
                expiresAt = "test_date_hour"
            )
        )

        coEvery {
            mockkAuthRepository.login(
                requestToken = requestToken,
                user = user
            )
        } returns ApiResponse.Error("Error")

        viewModel.login(user)

        coVerify(exactly = 1) { mockkAuthRepository.getRequestKey() }
        coVerify(exactly = 1) { mockkAuthRepository.login(requestToken, user) }

        assert(viewModel.loginResult.value is DataState.Error)
    }

    @Test
    fun `login failure due to network error`() = runBlocking {
        coEvery { mockkAuthRepository.getRequestKey() } returns ApiResponse.Error("Network Error")
        coEvery {
            mockkAuthRepository.login(
                any(),
                any()
            )
        } returns ApiResponse.Error("Network Error")

        viewModel.login(user)

        coVerify(exactly = 1) { mockkAuthRepository.getRequestKey() }
        coVerify(exactly = 0) { mockkAuthRepository.login(requestToken, user) }
        coVerify(exactly = 0) { mockkAuthRepository.createSessionId(any()) }

        assert(viewModel.loginResult.value is DataState.Error)
    }
}