package com.adesso.moveeapp.data.remote.service

import com.adesso.moveeapp.data.model.addratingmodel.AddRatingBodyModel
import com.adesso.moveeapp.data.model.addratingmodel.AddRatingModel
import com.adesso.moveeapp.data.model.creditsmodel.CreditsModel
import com.adesso.moveeapp.data.model.genresmodel.GenresModel
import com.adesso.moveeapp.data.model.singletvseriesmodel.SingleTvSeriesModel
import com.adesso.moveeapp.data.model.tvseriesmodel.TvSeriesModel
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface TvSeriesService {
    @GET(Constants.POPULAR_TV_SERIES_PATH)
    suspend fun getPopularTvSeries(
        @Query("page") pageNumber: Int
    ): Response<TvSeriesModel>

    @GET(Constants.TOP_RATED_TV_SERIES_PATH)
    suspend fun getTopRatedTvSeries(
        @Query("page") pageNumber: Int
    ): Response<TvSeriesModel>

    @GET(Constants.TV_SERIES_ALL_GENRES_PATH)
    suspend fun getGenres() : Response<GenresModel>

    @GET(Constants.SINGLE_TV_SERIES_PATH)
    suspend fun getSingleTvSeriesInfo(
        @Path("series_id") seriesId: Int
    ): Response<SingleTvSeriesModel>

    @GET(Constants.TV_CREDITS_PATH)
    suspend fun getTvSeriesCredits(
        @Path("series_id") seriesId: Int
    ): Response<CreditsModel>

    @POST(Constants.ADD_TV_RATING_PATH)
    suspend fun voteTv(
        @Path("series_id") movieId: Int,
        @Query("session_id") sessionId: String,
        @Body vote: AddRatingBodyModel
    ): Response<AddRatingModel>
}