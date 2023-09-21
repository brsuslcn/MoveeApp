package com.adesso.moveeapp.data.remote.service

import com.adesso.moveeapp.data.model.accountmodel.AccountInfoModel
import com.adesso.moveeapp.data.model.accountstatemodel.AccountStateModel
import com.adesso.moveeapp.data.model.addfavoritesmodel.AddFavoritesBodyModel
import com.adesso.moveeapp.data.model.addfavoritesmodel.AddFavoritesModel
import com.adesso.moveeapp.data.model.deletesessionmodel.DeleteSessionModel
import com.adesso.moveeapp.data.model.deletesessionmodel.DeleteSessionBodyModel
import com.adesso.moveeapp.data.model.moviesmodel.MoviesModel
import com.adesso.moveeapp.data.model.tvseriesmodel.TvSeriesModel
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.HTTP
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface AccountService {
    @GET(Constants.ACCOUNT_DETAIL_PATH)
    suspend fun getAccountInfo(
       @Query("session_id") sessionId: String
    ) : Response<AccountInfoModel>

    @HTTP(method = "DELETE", path = Constants.ACCOUNT_DELETE_SESSION_PATH, hasBody = true)
    suspend fun deleteSession(
        @Body sessionId: DeleteSessionBodyModel
    ): Response<DeleteSessionModel>

    @POST(Constants.ACCOUNT_ADD_FAVORITE_PATH)
    suspend fun changeFavorite(
        @Path("account_id") accountId: Int,
        @Query("session_id") sessionId: String,
        @Body item: AddFavoritesBodyModel
    ): Response<AddFavoritesModel>

    @GET(Constants.ACCOUNT_TV_FAVORITE_LIST_PATH)
    suspend fun getFavoriteTvList(
        @Path("account_id") accountId: Int,
        @Query("session_id") sessionId: String,
        @Query("page") page: Int
    ): Response<TvSeriesModel>

    @GET(Constants.ACCOUNT_MOVIE_FAVORITE_LIST_PATH)
    suspend fun getFavoriteMovieList(
        @Path("account_id") accountId: Int,
        @Query("session_id") sessionId: String,
        @Query("page") page: Int
    ): Response<MoviesModel>

    @GET(Constants.ACCOUNT_STATE_MOVIE_PATH)
    suspend fun getMovieState(
        @Path("movie_id") movieId: Int,
        @Query("session_id") sessionId: String
    ): Response<AccountStateModel>

    @GET(Constants.ACCOUNT_STATE_TV_PATH)
    suspend fun getTvState(
        @Path("series_id") seriesId: Int,
        @Query("session_id") sessionId: String
    ): Response<AccountStateModel>
}