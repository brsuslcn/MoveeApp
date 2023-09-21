package com.adesso.moveeapp

import com.adesso.moveeapp.data.model.accountstatemodel.AccountStateModel
import com.adesso.moveeapp.data.model.addfavoritesmodel.AddFavoritesBodyModel
import com.adesso.moveeapp.data.model.addfavoritesmodel.AddFavoritesModel
import com.adesso.moveeapp.data.model.addratingmodel.AddRatingBodyModel
import com.adesso.moveeapp.data.model.addratingmodel.AddRatingModel
import com.adesso.moveeapp.data.repository.AccountRepository
import com.adesso.moveeapp.data.repository.MoviesRepository
import com.adesso.moveeapp.data.repository.TvSeriesRepository
import com.adesso.moveeapp.ui.viewmodel.SharedViewModel
import com.adesso.moveeapp.util.ApiResponse
import com.adesso.moveeapp.util.Constants
import com.adesso.moveeapp.util.SessionManager
import com.adesso.moveeapp.util.state.DataState
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class SharedViewModelTest {
    private val mockAccountRepository = mockk<AccountRepository>()
    private val mockTvSeriesRepository = mockk<TvSeriesRepository>()
    private val mockMoviesRepository = mockk<MoviesRepository>()
    private val mockSessionManager = mockk<SessionManager>()

    private lateinit var viewModel : SharedViewModel
    private val accountId=123456
    private val sessionId="5f2e8a9b2cc14d7f9be01a91463d8a6e"

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        every { mockSessionManager.getRegisteredItem(Constants.PROFILE_ID) } returns accountId.toString()
        every { mockSessionManager.getRegisteredItem(Constants.SESSION_ID) } returns sessionId

        viewModel = SharedViewModel(mockAccountRepository, mockMoviesRepository, mockTvSeriesRepository, mockSessionManager)

        Dispatchers.setMain(Dispatchers.Unconfined)
    }


    @After
    fun cleanup() {
        Dispatchers.resetMain()
    }

    @Test
    fun `when user changes the favorite status if it works`() = runBlocking {
        val item = AddFavoritesBodyModel(Constants.MOVIE, 12345, true)

        coEvery { mockAccountRepository.changeFavorite(accountId, sessionId, item) } returns ApiResponse.Success(
            AddFavoritesModel(1, Constants.MOVIE, true))
        coEvery { mockAccountRepository.getMovieState(any(), any()) } returns ApiResponse.Success(
            AccountStateModel(true, 12345, Any(), Any() )
        )
        viewModel.changeFavorite(12345, Constants.MOVIE)
        assertEquals(DataState.Success(R.string.detail_screens_added_favorite_success), viewModel.isAddedFavoriteState.value)
    }

    @Test
    fun `getMovieVoteState should get voteState correctly`() = runBlocking {
        val movieId = 123
        val fakeVoteState = 4.0

        val map = mapOf("value" to fakeVoteState)

        coEvery { mockAccountRepository.getMovieState(movieId, sessionId) } returns ApiResponse.Success(AccountStateModel(
            id = movieId,
            rated = map,
            favorite = false,
            watchList = false
        ))

        viewModel.getMovieVoteState(movieId)
        assert(viewModel.voteState.value == fakeVoteState.toInt()/2)
    }

    @Test
    fun `vote should work correctly`() = runBlocking {
        val movieId = 1234
        val fakeVote = 3
        val voteBody = AddRatingBodyModel(fakeVote * 2)
        coEvery { mockMoviesRepository.voteMovie(movieId, sessionId, voteBody) } returns ApiResponse.Success(
            AddRatingModel(
                status_code = 200,
                status_message = "success",
                success = true
            )
        )

        viewModel.voteMovie(movieId,fakeVote)
        assert(viewModel.voteState.value == fakeVote)
    }
}