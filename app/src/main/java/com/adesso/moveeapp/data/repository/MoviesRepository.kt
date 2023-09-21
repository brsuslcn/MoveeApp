package com.adesso.moveeapp.data.repository

import androidx.paging.PagingData
import com.adesso.moveeapp.data.model.addratingmodel.AddRatingBodyModel
import com.adesso.moveeapp.data.model.addratingmodel.AddRatingModel
import com.adesso.moveeapp.data.model.creditsmodel.CreditsModel
import com.adesso.moveeapp.data.model.genresmodel.GenresModel
import com.adesso.moveeapp.data.model.moviesmodel.ResultMovie
import com.adesso.moveeapp.data.remote.service.MoviesService
import com.adesso.moveeapp.util.ApiResponse
import com.adesso.moveeapp.util.pagination.createPager
import com.adesso.moveeapp.data.model.singlemoviemodel.SingleMovieModel
import com.adesso.moveeapp.data.remote.network.BaseRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


class MoviesRepository @Inject constructor(private val moviesService: MoviesService): BaseRepository() {
    fun getPlayingMovies(): Flow<PagingData<ResultMovie>> = createPager { page ->
        moviesService.getNowPlayingMovies(page).body()!!.resultMovies
    }.flow

    fun getPopularMovies(): Flow<PagingData<ResultMovie>> = createPager { page ->
        moviesService.getPopularMovies(page).body()!!.resultMovies
    }.flow

    suspend fun getSingleMovie(movieId: Int) : ApiResponse<SingleMovieModel> {
        return safeApiRequest { moviesService.getSingleMovieInfo(movieId) }
    }

    suspend fun getMovieCredits(movieId: Int): ApiResponse<CreditsModel> {
        return safeApiRequest { moviesService.getMovieCredits(movieId) }
    }

    suspend fun getAllGenres() : ApiResponse<GenresModel> {
        return safeApiRequest { moviesService.getGenres() }
    }

    suspend fun voteMovie(movieId: Int, sessionId: String, value: AddRatingBodyModel): ApiResponse<AddRatingModel> {
        return safeApiRequest { moviesService.voteMovie(movieId, sessionId, value) }
    }
}