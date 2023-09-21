package com.adesso.moveeapp.data.repository

import androidx.paging.PagingData
import com.adesso.moveeapp.data.model.addratingmodel.AddRatingBodyModel
import com.adesso.moveeapp.data.model.addratingmodel.AddRatingModel
import com.adesso.moveeapp.data.model.creditsmodel.CreditsModel
import com.adesso.moveeapp.data.model.genresmodel.GenresModel
import com.adesso.moveeapp.data.model.singletvseriesmodel.SingleTvSeriesModel
import com.adesso.moveeapp.data.model.tvseriesmodel.Result
import com.adesso.moveeapp.data.remote.network.BaseRepository
import com.adesso.moveeapp.data.remote.service.TvSeriesService
import com.adesso.moveeapp.util.ApiResponse
import com.adesso.moveeapp.util.pagination.createPager
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class TvSeriesRepository @Inject constructor(private val tvSeriesService: TvSeriesService) :
    BaseRepository() {
    fun getPopularTvSeries(): Flow<PagingData<Result>> = createPager { page ->
        tvSeriesService.getPopularTvSeries(page).body()!!.results
    }.flow

    fun getTopRatedTvSeries(): Flow<PagingData<Result>> = createPager { page ->
        tvSeriesService.getTopRatedTvSeries(page).body()!!.results
    }.flow

    suspend fun getAllGenres(): ApiResponse<GenresModel> {
        return safeApiRequest { tvSeriesService.getGenres() }
    }

    suspend fun getTvSeriesInfo(seriesId: Int): ApiResponse<SingleTvSeriesModel> {
        return safeApiRequest { tvSeriesService.getSingleTvSeriesInfo(seriesId) }
    }

    suspend fun getTvSeriesCredits(seriesId: Int): ApiResponse<CreditsModel> {
        return safeApiRequest { tvSeriesService.getTvSeriesCredits(seriesId) }
    }

    suspend fun voteTv(seriesId: Int, sessionId: String, vote: AddRatingBodyModel): ApiResponse<AddRatingModel> {
        return safeApiRequest { tvSeriesService.voteTv(seriesId, sessionId, vote) }
    }
}