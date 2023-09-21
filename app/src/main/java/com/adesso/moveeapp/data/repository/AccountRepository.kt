package com.adesso.moveeapp.data.repository

import androidx.paging.PagingData
import com.adesso.moveeapp.data.model.accountmodel.AccountInfoModel
import com.adesso.moveeapp.data.model.accountstatemodel.AccountStateModel
import com.adesso.moveeapp.data.model.addfavoritesmodel.AddFavoritesBodyModel
import com.adesso.moveeapp.data.model.addfavoritesmodel.AddFavoritesModel
import com.adesso.moveeapp.data.model.deletesessionmodel.DeleteSessionBodyModel
import com.adesso.moveeapp.data.model.deletesessionmodel.DeleteSessionModel
import com.adesso.moveeapp.data.model.moviesmodel.ResultMovie
import com.adesso.moveeapp.data.model.tvseriesmodel.Result
import com.adesso.moveeapp.data.remote.network.BaseRepository
import com.adesso.moveeapp.data.remote.service.AccountService
import com.adesso.moveeapp.util.ApiResponse
import com.adesso.moveeapp.util.pagination.createPager
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class AccountRepository @Inject constructor(private val accountService: AccountService) :
    BaseRepository() {
    suspend fun getAccountInfo(sessionId: String): ApiResponse<AccountInfoModel> {
        return safeApiRequest { accountService.getAccountInfo(sessionId) }
    }

    suspend fun deleteSession(sessionId: DeleteSessionBodyModel): ApiResponse<DeleteSessionModel> {
        return safeApiRequest { accountService.deleteSession(sessionId) }
    }

    suspend fun changeFavorite(
        accountId: Int,
        sessionId: String,
        item: AddFavoritesBodyModel
    ): ApiResponse<AddFavoritesModel> {
        return safeApiRequest { accountService.changeFavorite(accountId, sessionId, item) }
    }

    suspend fun getMovieState(movieId: Int, sessionId: String): ApiResponse<AccountStateModel> {
        return safeApiRequest { accountService.getMovieState(movieId, sessionId) }
    }

    suspend fun getTvState(seriesId: Int, sessionId: String): ApiResponse<AccountStateModel> {
        return safeApiRequest { accountService.getTvState(seriesId, sessionId) }
    }

    fun getFavoriteTvList(accountId: Int, sessionId: String): Flow<PagingData<Result>> = createPager { page ->
        accountService.getFavoriteTvList(accountId, sessionId, page).body()!!.results
    }.flow

    fun getFavoriteMovieList(accountId: Int, sessionId: String): Flow<PagingData<ResultMovie>>
    = createPager { page ->
        accountService.getFavoriteMovieList(accountId, sessionId, page).body()!!.resultMovies
    }.flow
}