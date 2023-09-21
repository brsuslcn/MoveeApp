package com.adesso.moveeapp.data.remote.service

import com.adesso.moveeapp.data.model.addratingmodel.AddRatingBodyModel
import com.adesso.moveeapp.data.model.addratingmodel.AddRatingModel
import com.adesso.moveeapp.data.model.creditsmodel.CreditsModel
import com.adesso.moveeapp.data.model.genresmodel.GenresModel
import com.adesso.moveeapp.data.model.moviesmodel.MoviesModel
import com.adesso.moveeapp.data.model.singlemoviemodel.SingleMovieModel
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface MoviesService {
    @GET(Constants.NOW_PLAYING_PATH)
    suspend fun getNowPlayingMovies(
        @Query("page") pageNumber: Int
    ) : Response<MoviesModel>

    @GET(Constants.POPULAR_PATH)
    suspend fun getPopularMovies(
        @Query("page") pageNumber: Int
    ) : Response<MoviesModel>
    @GET(Constants.SINGLE_MOVIE_PATH)
    suspend fun getSingleMovieInfo(
        @Path("movie_id") movieId: Int
    ): Response<SingleMovieModel>

    @GET(Constants.MOVIES_ALL_GENRES_PATH)
    suspend fun getGenres(
    ): Response<GenresModel>

    @GET(Constants.MOVIE_CREDITS_PATH)
    suspend fun getMovieCredits(
        @Path("movie_id") movieId: Int
    ): Response<CreditsModel>

    @POST(Constants.ADD_MOVIE_RATING_PATH)
    suspend fun voteMovie(
        @Path("movie_id") movieId: Int,
        @Query("session_id") sessionId: String,
        @Body vote: AddRatingBodyModel
    ): Response<AddRatingModel>
}